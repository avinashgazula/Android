using Umbraco.Cms.Core.Composing;
using Umbraco.Cms.Core.Notifications;

namespace BlockPreviewDemo.Infrastructure;

public sealed class DemoComposer : IComposer
{
    public void Compose(IUmbracoBuilder builder) =>
        builder.AddNotificationAsyncHandler<UmbracoApplicationStartedNotification, DemoContentSeeder>();
}
