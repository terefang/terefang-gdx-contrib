## terefang-gdx-contrib-gdf

A GdfBitmapFont to be used insted of BitmapFont providing load capability of the .gdf (libgd bitmap font) format and the
.gdfa (ascii developers variant) format both in raw and gzipped variants.

GdfBitmapFont also includes an internal 8x8 ascii 7-bit font for fallback, if fontfile isnt found or null.

### Examples

see src/test/java/TestGdf.java for an example how to use.

### Internal Font

internal 8x8 font -- ported from c-source of libungif -- https://github.com/Distrotech/libungif/blob/master/COPYING