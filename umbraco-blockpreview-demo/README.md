# BlockPreview Stylesheet Race Condition — Umbraco Demo

Reproduces the bug in [rickbutterfield/BlockPreview#287](https://github.com/rickbutterfield/BlockPreview/issues/287)
where block preview styles are missing in the backoffice until the page is
reloaded with DevTools open.

## Stack

| Package | Version |
|---------|---------|
| Umbraco.Cms | 17.x (net10.0) |
| Umbraco.Community.BlockPreview | 5.3.2 |

## Running the project

```bash
cd BlockPreviewDemo
DOTNET_ROLL_FORWARD=LatestMajor dotnet run
```

Then open http://localhost:5000/umbraco and log in:

| Field | Value |
|-------|-------|
| Email | admin@example.com |
| Password | Admin1234! |

## Reproducing the bug

1. In the backoffice, go to **Settings → Document Types**
2. Create a Document Type called **"Home"** with:
   - A property of type **Block Grid**
   - Add two Block Types to it: **Hero Block** and **Text Block**
   - Set the Element Type aliases to `heroBlock` and `textBlock`
3. Go to **Content**, create a **Home** page (do **not** save yet)
4. Add a Hero Block or Text Block to the block grid property
5. Observe: the block preview renders **without styles** in the backoffice
6. Open DevTools (`F12`) and refresh — styles now appear

The block views are in `Views/BlockGrid/Components/` and reference
`/css/blocks.css`. The stylesheet is fetched and adopted into the Lit
element's shadow root via `adoptedStyleSheets`. On fast machines the
preview API response arrives before the stylesheet adoption completes,
so markup is painted unstyled.

## The fix

In `block-preview-base.element.ts` (BlockPreview package source):

**1. Deduplicate the stylesheet loading Promise:**

```typescript
private _stylesheetLoadingPromise?: Promise<void>;

protected fetchAndLoadStylesheets(): Promise<void> {
    if (this._stylesAdopted || !this._blockPreviewContext) return Promise.resolve();

    if (!this._stylesheetLoadingPromise) {
        this._stylesheetLoadingPromise = (async () => {
            const data = await this.fetchStylesheets();
            if (data && data.length > 0) {
                const sheets = await Promise.all(
                    data.map(href => this._blockPreviewContext!.getOrCreateStylesheet(href))
                );
                const shadowRoot = this.renderRoot as ShadowRoot;
                shadowRoot.adoptedStyleSheets = [...shadowRoot.adoptedStyleSheets, ...sheets];
            }
            this._stylesAdopted = true;
        })();
    }

    return this._stylesheetLoadingPromise;
}
```

**2. Await stylesheets before rendering markup:**

```diff
 protected async renderBlockPreview() {
   if (!this._isConnected) return;
   this.resolveUniqueFromContext();
   if (!this.validatePreviewData()) return;
+  await this.fetchAndLoadStylesheets();
   this._isLoading = true;
```

This keeps CSS fetch and the preview API call running in parallel
(the fetch is started early in `handleWorkspaceData`) but guarantees
the shadow root has its stylesheet before any markup is painted.
