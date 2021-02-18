## terefang-gdx-contrib-gdf

A GdfBitmapFont to be used insted of BitmapFont providing load capability of the .gdf (libgd bitmap font) format and the
.gdfa (ascii developers variant) format both in raw and gzipped variants.

GdfBitmapFont also includes an internal 8x8 ascii 7-bit font for fallback, if fontfile isnt found or null.

### Example

```
font = GdfBitmapFont.create((f)->Gdx.files.internal(f), "display.gdf");
...
font.setColor(Color.ROYAL);
...
batch.begin();
font.draw(batch, "Hello World !!!", 200, 200);
batch.end();
...
```

Note: GdfBitmapFont needs to know the filename extension to use the right format (or gzip) loader.

### Internal Font

internal 8x8 font -- ported from c-source of libungif -- https://github.com/Distrotech/libungif/blob/master/COPYING

