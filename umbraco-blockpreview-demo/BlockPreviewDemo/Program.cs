
using Umbraco.Community.BlockPreview.Extensions;

WebApplicationBuilder builder = WebApplication.CreateBuilder(args);

builder.CreateUmbracoBuilder()
    .AddBackOffice()
    .AddWebsite()
    .AddComposers()
    .AddBlockPreview(options =>
    {
        options.BlockGrid = new()
        {
            Enabled = true,
            Stylesheets = ["/css/blocks.css"]
        };
        options.BlockList = new()
        {
            Enabled = true,
            Stylesheets = ["/css/blocks.css"]
        };
    })
    .Build();

WebApplication app = builder.Build();


await app.BootUmbracoAsync();


app.UseUmbraco()
    .WithMiddleware(u =>
    {
        u.UseBackOffice();
        u.UseWebsite();
    })
    .WithEndpoints(u =>
    {
        u.UseBackOfficeEndpoints();
        u.UseWebsiteEndpoints();
    });

await app.RunAsync();
