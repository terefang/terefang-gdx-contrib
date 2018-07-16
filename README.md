## terefang-gdx-contrib

terefangs inofficial contributions to libgdx

### versioning

there will be at most a monthly release so version follows th major/minor version of libgdx it was tested with,
PLUS year and month of the release OPTIONALLY followed by a patch/fix identifier.

since libgdx versioning is m.n.x, contrib versioning will be m.n.yyyy.mm[.fix].

### Modules

#### contrib-gdf

A GdfBitmapFont to be used insted of BitmapFont providing load capability of the .gdf (libgd bitmap font) format and the
.gdfa (ascii developers variant) format both in raw and gzipped variants.

GdfBitmapFont also includes an internal 8x8 ascii 7-bit font for fallback, if fontfile isnt found or null.

see modules README for further details.

#### contrib-gdf-*fonts

classpath packed fonts

see modules README for further details.

### usage

```xml

    <repositories>
        <repository>
            <id>terefang-gdx-contrib-maven-repo</id>
            <url>https://raw.githubusercontent.com/terefang/terefang-gdx-contrib/master/maven-repo/</url>
        </repository>
    </repositories>

```
