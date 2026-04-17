using Umbraco.Cms.Core;
using Umbraco.Cms.Core.Events;
using Umbraco.Cms.Core.Models;
using Umbraco.Cms.Core.Models.ContentTypeEditing;
using Umbraco.Cms.Core.Notifications;
using Umbraco.Cms.Core.PropertyEditors;
using Umbraco.Cms.Core.PropertyEditors.ValueConverters;
using Umbraco.Cms.Core.Serialization;
using Umbraco.Cms.Core.Services;
using Umbraco.Cms.Core.Services.ContentTypeEditing;

namespace BlockPreviewDemo.Infrastructure;

/// <summary>
/// Creates element types (heroBlock, textBlock), a Block Grid data type, and a Home
/// document type on first startup. All work is skipped on subsequent restarts via
/// well-known keys so the seeder is fully idempotent.
/// </summary>
public sealed class DemoContentSeeder : INotificationAsyncHandler<UmbracoApplicationStartedNotification>
{
    private static readonly Guid HeroBlockKey   = new("11111111-0000-0000-0000-000000000001");
    private static readonly Guid TextBlockKey   = new("11111111-0000-0000-0000-000000000002");
    private static readonly Guid BlockGridDtKey = new("11111111-0000-0000-0000-000000000003");
    private static readonly Guid HomeDocTypeKey = new("11111111-0000-0000-0000-000000000004");

    private readonly IContentTypeService        _contentTypeService;
    private readonly IContentTypeEditingService _contentTypeEditingService;
    private readonly IDataTypeService           _dataTypeService;
    private readonly PropertyEditorCollection   _propertyEditors;
    private readonly IConfigurationEditorJsonSerializer _serializer;
    private readonly ILogger<DemoContentSeeder> _logger;

    public DemoContentSeeder(
        IContentTypeService contentTypeService,
        IContentTypeEditingService contentTypeEditingService,
        IDataTypeService dataTypeService,
        PropertyEditorCollection propertyEditors,
        IConfigurationEditorJsonSerializer serializer,
        ILogger<DemoContentSeeder> logger)
    {
        _contentTypeService        = contentTypeService;
        _contentTypeEditingService = contentTypeEditingService;
        _dataTypeService           = dataTypeService;
        _propertyEditors           = propertyEditors;
        _serializer                = serializer;
        _logger                    = logger;
    }

    public async Task HandleAsync(
        UmbracoApplicationStartedNotification notification,
        CancellationToken cancellationToken)
    {
        if (_contentTypeService.Get("home") is not null)
            return;

        _logger.LogInformation("[BlockPreview demo] Seeding content types…");

        // Resolve built-in Textstring and Textarea data type keys at runtime.
        var textStringKey  = await GetBuiltInDataTypeKeyAsync(Constants.PropertyEditors.Aliases.TextBox);
        var textAreaKey    = await GetBuiltInDataTypeKeyAsync(Constants.PropertyEditors.Aliases.TextArea);

        var heroBlock  = await CreateElementTypeAsync("heroBlock", "Hero Block", "icon-science color-deep-purple", HeroBlockKey,  textStringKey, textAreaKey);
        var textBlock  = await CreateElementTypeAsync("textBlock", "Text Block", "icon-document color-blue",       TextBlockKey, textStringKey, textAreaKey);
        var blockGridDt = await CreateBlockGridDataTypeAsync(heroBlock, textBlock);
        await CreateHomeDocumentTypeAsync(blockGridDt);

        _logger.LogInformation("[BlockPreview demo] Seeding complete — Home, heroBlock, textBlock created.");
    }

    // ── Helpers ──────────────────────────────────────────────────────────────

    private async Task<Guid> GetBuiltInDataTypeKeyAsync(string editorAlias)
    {
        var types = await _dataTypeService.GetByEditorAliasAsync(editorAlias);
        var dt = types.FirstOrDefault()
            ?? throw new InvalidOperationException($"Built-in data type for editor '{editorAlias}' not found.");
        return dt.Key;
    }

    // ── Element type ─────────────────────────────────────────────────────────

    private async Task<IContentType> CreateElementTypeAsync(
        string alias, string name, string icon, Guid key,
        Guid textStringKey, Guid textAreaKey)
    {
        var existing = _contentTypeService.Get(alias);
        if (existing is not null) return existing;

        var groupKey = Guid.NewGuid();

        var model = new ContentTypeCreateModel
        {
            Key           = key,
            Alias         = alias,
            Name          = name,
            Icon          = icon,
            IsElement     = true,
            AllowedAsRoot = false,
            Containers    =
            [
                new ContentTypePropertyContainerModel
                {
                    Key       = groupKey,
                    Name      = "Content",
                    Type      = "Group",
                    SortOrder = 0
                }
            ],
            Properties    =
            [
                new ContentTypePropertyTypeModel
                {
                    Key          = Guid.NewGuid(),
                    ContainerKey = groupKey,
                    Alias        = "title",
                    Name         = "Title",
                    DataTypeKey  = textStringKey,
                    SortOrder    = 0
                },
                new ContentTypePropertyTypeModel
                {
                    Key          = Guid.NewGuid(),
                    ContainerKey = groupKey,
                    Alias        = "description",
                    Name         = "Description",
                    DataTypeKey  = textAreaKey,
                    SortOrder    = 1
                }
            ]
        };

        var result = await _contentTypeEditingService.CreateAsync(model, Constants.Security.SuperUserKey);

        if (!result.Success)
            throw new InvalidOperationException($"Failed to create element type '{alias}': {result.Status}");

        _logger.LogInformation("[BlockPreview demo] Created element type: {Alias}", alias);
        return result.Result!;
    }

    // ── Block Grid data type ─────────────────────────────────────────────────

    private async Task<IDataType> CreateBlockGridDataTypeAsync(
        IContentType heroBlock, IContentType textBlock)
    {
        var existing = await _dataTypeService.GetAsync(BlockGridDtKey);
        if (existing is not null) return existing;

        if (!_propertyEditors.TryGet(Constants.PropertyEditors.Aliases.BlockGrid, out var blockGridEditor))
            throw new InvalidOperationException("Block Grid property editor not registered.");

        var config = new BlockGridConfiguration
        {
            GridColumns = 12,
            Blocks =
            [
                new BlockGridConfiguration.BlockGridBlockConfiguration
                {
                    ContentElementTypeKey = heroBlock.Key,
                    AllowAtRoot           = true,
                    AllowInAreas          = false,
                    Areas                 = []
                },
                new BlockGridConfiguration.BlockGridBlockConfiguration
                {
                    ContentElementTypeKey = textBlock.Key,
                    AllowAtRoot           = true,
                    AllowInAreas          = false,
                    Areas                 = []
                }
            ]
        };

        var configData = blockGridEditor!
            .GetConfigurationEditor()
            .FromConfigurationObject(config, _serializer);

        var dataType = new DataType(blockGridEditor, _serializer)
        {
            Key          = BlockGridDtKey,
            Name         = "Demo Block Grid",
            DatabaseType = ValueStorageType.Ntext
        };
        dataType.SetConfigurationData(configData);

        var dtResult = await _dataTypeService.CreateAsync(dataType, Constants.Security.SuperUserKey);
        if (!dtResult.Success)
            throw new InvalidOperationException($"Failed to create Block Grid data type: {dtResult.Status}");

        _logger.LogInformation("[BlockPreview demo] Created data type: Demo Block Grid");
        return dataType;
    }

    // ── Home document type ────────────────────────────────────────────────────

    private async Task CreateHomeDocumentTypeAsync(IDataType blockGridDt)
    {
        if (_contentTypeService.Get("home") is not null) return;

        var groupKey = Guid.NewGuid();

        var model = new ContentTypeCreateModel
        {
            Key           = HomeDocTypeKey,
            Alias         = "home",
            Name          = "Home",
            Icon          = "icon-home color-blue",
            IsElement     = false,
            AllowedAsRoot = true,
            Containers    =
            [
                new ContentTypePropertyContainerModel
                {
                    Key       = groupKey,
                    Name      = "Content",
                    Type      = "Group",
                    SortOrder = 0
                }
            ],
            Properties    =
            [
                new ContentTypePropertyTypeModel
                {
                    Key          = Guid.NewGuid(),
                    ContainerKey = groupKey,
                    Alias        = "pageContent",
                    Name         = "Page Content",
                    DataTypeKey  = blockGridDt.Key,
                    SortOrder    = 0
                }
            ]
        };

        var result = await _contentTypeEditingService.CreateAsync(model, Constants.Security.SuperUserKey);

        if (!result.Success)
            throw new InvalidOperationException($"Failed to create Home document type: {result.Status}");

        _logger.LogInformation("[BlockPreview demo] Created document type: home");
    }
}
