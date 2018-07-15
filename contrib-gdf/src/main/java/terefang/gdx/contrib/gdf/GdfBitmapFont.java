/*
 * Copyright (c) 2018. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package terefang.gdx.contrib.gdf;

import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.PixmapTextureData;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;

public class GdfBitmapFont extends BitmapFont
{
	public static final BitmapFont create(FileHandleResolver resolver, String fileName) throws IOException
	{
		BitmapFontData data = new BitmapFontData();
		Gdx2DPixmap pixmap = null;
		InputStream ins = null;
		FileHandle res = null;
		if(resolver!=null)
		{
			res = resolver.resolve(fileName);
			if(res != null)
			{
				try
				{
					ins = res.read();
				}
				catch(Exception xe)
				{
					res = null;
					ins = null;
				}
			}
		}
		
		if(fileName == null || ins == null)
		{
			pixmap = createFromInternal(data);
		}
		else
		if(fileName.endsWith(".gdfa"))
		{
			pixmap = createFromGDFA(data, ins);
		}
		else
		if(fileName.endsWith(".gdfa.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFA(data, ins);
		}
		else
		if(fileName.endsWith(".gdf"))
		{
			pixmap = createFromGDFB(data, ins, false);
		}
		else
		if(fileName.endsWith(".gdf.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFB(data, ins, false);
		}
		else
		if(fileName.endsWith(".gdfx"))
		{
			pixmap = createFromGDFB(data, ins, true);
		}
		else
		if(fileName.endsWith(".gdfx.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFB(data, ins, true);
		}
		else
		{
			throw new IllegalArgumentException("Unknown Font Format = "+fileName);
		}
		
		Texture texture = new Texture((TextureData)(new PixmapTextureData(new Pixmap(pixmap), (Pixmap.Format)null, false, true)));
		TextureRegion region = new TextureRegion(texture);
		
		return new GdfBitmapFont(data, region);
	}
	
	public static final Gdx2DPixmap createPixmap(int w, int h)
	{
		Gdx2DPixmap p = new Gdx2DPixmap(w, h, Gdx2DPixmap.GDX2D_FORMAT_LUMINANCE_ALPHA);
		p.clear(0);
		return p;
	}
	
	public static final void setBmData(BitmapFontData data, int startChar, int numChars, int charH, int charW, int charHstep, int charWstep, int charsPerRow, int[] charWadvance)
	{
		data.padTop = 0;
		data.padRight = 1;
		data.padBottom = 0;
		data.padLeft = 0;
		data.lineHeight = charH;
		data.imagePaths = new String[0];
		data.descent = 0.0F;
		data.spaceWidth = (float)charW+1;
		data.xHeight = (charH/2)+1;
		data.capHeight = charH;
		data.ascent = 0;
		data.down = -data.lineHeight;
		data.descent = 0;
		
		Glyph glyph;
		int ci = 0;
		for(int c = startChar; c < (numChars+startChar); c++)
		{
			glyph = new Glyph();
			glyph.id = c;
			if(charWadvance != null)
			{
				glyph.xadvance = charWadvance[ci];
			}
			else
			{
				glyph.xadvance = charW+1;
			}
			glyph.fixedWidth = true;
			glyph.height = charH;
			glyph.width = charW;
			glyph.srcX = (ci % charsPerRow) * charWstep;
			glyph.srcY = (ci / charsPerRow) * charHstep;
			glyph.page = 0;
			data.setGlyph(glyph.id, glyph);
			ci++;
		}
		data.missingGlyph = data.getGlyph((char) 0);
		if(data.missingGlyph == null)
		{
			data.missingGlyph = data.getGlyph(' ');
		}
	}
	
	public static final Gdx2DPixmap createFromInternal(BitmapFontData data) throws IOException
	{
		int startChar = 0;
		int numChars = 128;
		int charH = 8;
		int charW = 8;
		int charWstep = 16;
		int charHstep = 16;
		
		Gdx2DPixmap pixmap = createPixmap(256, 256);
		
		setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, 16, null);
		
		for(int c = startChar; c < (startChar+numChars); c++)
		{
			Glyph glyph = data.getGlyph((char) c);
			int srcX = glyph.srcX;
			int srcY = glyph.srcY;
			for(int y = 0 ; y < charH; y++)
			{
				int mask = 0x80;
				for(int x = 0 ; x < charW; x++)
				{ ;
					pixmap.setPixel(srcX + x, srcY + y, ((AsciiTable[c-startChar][y] & mask) == 0 ? 0 : 0xFFFFffff));
					mask >>= 1;
				}
			}
		}
		return pixmap;
	}
	
	public static int nextPowerOf2(int a)
	{
		int b = 1;
		while(b < a)
		{
			b <<= 1;
		}
		return b;
	}
	
	public static final Gdx2DPixmap createFromGDFA(BitmapFontData data, InputStream ins) throws IOException
	{
		Gdx2DPixmap pixmap = null;
		int startChar = 0;
		int numChars = 0;
		int charW = 0;
		int charH = 0;

		StreamTokenizer st = new StreamTokenizer(ins);
		st.slashStarComments(true);
		st.slashSlashComments(true);
		st.parseNumbers();
		st.whitespaceChars(0, 0x20);
		st.whitespaceChars(',', ',');
		st.wordChars(0x40, 0x5A);
		st.wordChars(0x60, 0x7A);
		
		st.nextToken();
		if("GDFA".equalsIgnoreCase(st.sval))
		{
			st.nextToken();
			startChar = (int) st.nval;
			st.nextToken();
			numChars = (int) st.nval;
			st.nextToken();
			charW = (int) st.nval;
			st.nextToken();
			charH = (int) st.nval;
			
			int charWstep = nextPowerOf2(charW);
			int charHstep = nextPowerOf2(charH);
			
			int numpChars = nextPowerOf2(numChars);
			int totalArea = nextPowerOf2(charHstep*charWstep*numpChars);
			int texRes = Integer.numberOfTrailingZeros(totalArea)+1;
			texRes /= 2;
			texRes = 1 << texRes;
			int charPerRow = texRes/charWstep;
			
			pixmap = createPixmap(texRes, texRes);
			
			setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, charPerRow, null);
			
			int ci = 0;
			for(int c = startChar; c < (startChar+numChars); c++)
			{
				Glyph glyph = data.getGlyph((char) c);
				int srcX = glyph.srcX;
				int srcY = glyph.srcY;
				for(int y = 0 ; y < charH; y++)
				{
					for(int x = 0 ; x < charW; x++)
					{
						st.nextToken();
						pixmap.setPixel(srcX + x, srcY + y, (st.nval == 0 ? 0 : 0xFFFFffff));
						//	System.err.print(st.nval == 0 ? "." : "*");
					}
					//System.err.println();
				}
				ci++;
			}
			ins.close();
		}
		else
		{
			throw new IllegalArgumentException("Unknown Font Format = "+st.sval);
		}
		return pixmap;
	}
	
	public static final Gdx2DPixmap createFromGDFB(BitmapFontData data, InputStream ins, boolean extendedFormat) throws IOException
	{
		/* Only supports a architecture-dependent binary dump format
		 * at the moment.
		 * The file format is like this on machines with 32-byte integers:
		 *
		 * byte 0-3:   (int) number of characters in the font
		 * byte 4-7:   (int) value of first character in the font (often 32, space)
		 * byte 8-11:  (int) pixel width of each character
		 * byte 12-15: (int) pixel height of each character
		 * bytes 16-:  (char) array with character data, one byte per pixel
		 *                    in each character, for a total of
		 *                    (nchars*width*height) bytes.
		 */
		/*---
		 *
		 *  extended format has byte[numofChars] charAdvanceWidth @ 0x10
		 *  character data follows and is considered greyscale (anti-aliased)
		 *  but 0x01 is considered full color in lecacy/interim mode.
		 *
		 */
		EndianDataInputStream edis = new EndianDataInputStream(ins);
		edis.order(ByteOrder.BIG_ENDIAN);
		int numChars = edis.readInt();
		if(numChars != (numChars & 0xffff))
		{
			edis.order(ByteOrder.LITTLE_ENDIAN);
			numChars = ((numChars>>>24)&0xff) | ((numChars>>>8)&0xff00);
		}
		
		int startChar = edis.readInt();
		int charW = edis.readInt();
		int charH = edis.readInt();
		
		int charWstep = nextPowerOf2(charW);
		int charHstep = nextPowerOf2(charH);
		
		int numpChars = nextPowerOf2(numChars);
		int totalArea = nextPowerOf2(charHstep*charWstep*numpChars);
		int texRes = Integer.numberOfTrailingZeros(totalArea)+1;
		texRes /= 2;
		texRes = 1 << texRes;
		int charPerRow = texRes/charWstep;
		
		Gdx2DPixmap pixmap = createPixmap(texRes, texRes);
		
		// ext format contains real char widths in front of bitmap data
		if(extendedFormat)
		{
			int[] charAdvance = new int[numChars];
			for(int ci = 0; ci < numChars; ci++)
			{
				charAdvance[ci] = (edis.readByte() & 0xff);
			}
			setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, charPerRow, charAdvance);
		}
		else
		{
			setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, charPerRow, null);
		}

		int ci = 0;
		for(int c = startChar; c < (startChar+numChars); c++)
		{
			Glyph glyph = data.getGlyph((char) c);
			int srcX = glyph.srcX;
			int srcY = glyph.srcY;
			
			// ext format is grey-scale
			if(extendedFormat)
			{
				for(int y = 0 ; y < charH; y++)
				{
					for(int x = 0 ; x < charW; x++)
					{ ;
						long p = edis.readByte() & 0xff;
						if(p == 1L)
						{
							/* there is no good grey-scale at 0x01 */
							/* so set to full color for legacy/interim format */
							p = 0xFFFFffffL;
						}
						else
						{
							p |= (p << 8);
							p |= (p << 16);
						}
						pixmap.setPixel(srcX + x, srcY + y, (int) (p & 0xFFFFffff));
					}
				}
			}
			else
			{
				for(int y = 0 ; y < charH; y++)
				{
					for(int x = 0 ; x < charW; x++)
					{ ;
						pixmap.setPixel(srcX + x, srcY + y, (edis.readByte() == 0 ? 0 : 0xFFFFffff));
					}
				}
			}
			ci++;
		}
		edis.close();
		return pixmap;
	}

	public GdfBitmapFont(BitmapFontData data, TextureRegion region)
	{
		super(data, region, false);
	}
	
	public static int AsciiTable[][] = {
		{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},    /* Ascii 0 */
		{0x3c, 0x42, 0xa5, 0x81, 0xbd, 0x42, 0x3c, 0x00},    /* Ascii 1 */
		{0x3c, 0x7e, 0xdb, 0xff, 0xc3, 0x7e, 0x3c, 0x00},    /* Ascii 2 */
		{0x00, 0xee, 0xfe, 0xfe, 0x7c, 0x38, 0x10, 0x00},    /* Ascii 3 */
		{0x10, 0x38, 0x7c, 0xfe, 0x7c, 0x38, 0x10, 0x00},    /* Ascii 4 */
		{0x00, 0x3c, 0x18, 0xff, 0xff, 0x08, 0x18, 0x00},    /* Ascii 5 */
		{0x10, 0x38, 0x7c, 0xfe, 0xfe, 0x10, 0x38, 0x00},    /* Ascii 6 */
		{0x00, 0x00, 0x18, 0x3c, 0x18, 0x00, 0x00, 0x00},    /* Ascii 7 */
		{0xff, 0xff, 0xe7, 0xc3, 0xe7, 0xff, 0xff, 0xff},    /* Ascii 8 */
		{0x00, 0x3c, 0x42, 0x81, 0x81, 0x42, 0x3c, 0x00},    /* Ascii 9 */
		{0xff, 0xc3, 0xbd, 0x7e, 0x7e, 0xbd, 0xc3, 0xff},    /* Ascii 10 */
		{0x1f, 0x07, 0x0d, 0x7c, 0xc6, 0xc6, 0x7c, 0x00},    /* Ascii 11 */
		{0x00, 0x7e, 0xc3, 0xc3, 0x7e, 0x18, 0x7e, 0x18},    /* Ascii 12 */
		{0x04, 0x06, 0x07, 0x04, 0x04, 0xfc, 0xf8, 0x00},    /* Ascii 13 */
		{0x0c, 0x0a, 0x0d, 0x0b, 0xf9, 0xf9, 0x1f, 0x1f},    /* Ascii 14 */
		{0x00, 0x92, 0x7c, 0x44, 0xc6, 0x7c, 0x92, 0x00},    /* Ascii 15 */
		{0x00, 0x00, 0x60, 0x78, 0x7e, 0x78, 0x60, 0x00},    /* Ascii 16 */
		{0x00, 0x00, 0x06, 0x1e, 0x7e, 0x1e, 0x06, 0x00},    /* Ascii 17 */
		{0x18, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x7e, 0x18},    /* Ascii 18 */
		{0x66, 0x66, 0x66, 0x66, 0x66, 0x00, 0x66, 0x00},    /* Ascii 19 */
		{0xff, 0xb6, 0x76, 0x36, 0x36, 0x36, 0x36, 0x00},    /* Ascii 20 */
		{0x7e, 0xc1, 0xdc, 0x22, 0x22, 0x1f, 0x83, 0x7e},    /* Ascii 21 */
		{0x00, 0x00, 0x00, 0x7e, 0x7e, 0x00, 0x00, 0x00},    /* Ascii 22 */
		{0x18, 0x7e, 0x18, 0x18, 0x7e, 0x18, 0x00, 0xff},    /* Ascii 23 */
		{0x18, 0x7e, 0x18, 0x18, 0x18, 0x18, 0x18, 0x00},    /* Ascii 24 */
		{0x18, 0x18, 0x18, 0x18, 0x18, 0x7e, 0x18, 0x00},    /* Ascii 25 */
		{0x00, 0x04, 0x06, 0xff, 0x06, 0x04, 0x00, 0x00},    /* Ascii 26 */
		{0x00, 0x20, 0x60, 0xff, 0x60, 0x20, 0x00, 0x00},    /* Ascii 27 */
		{0x00, 0x00, 0x00, 0xc0, 0xc0, 0xc0, 0xff, 0x00},    /* Ascii 28 */
		{0x00, 0x24, 0x66, 0xff, 0x66, 0x24, 0x00, 0x00},    /* Ascii 29 */
		{0x00, 0x00, 0x10, 0x38, 0x7c, 0xfe, 0x00, 0x00},    /* Ascii 30 */
		{0x00, 0x00, 0x00, 0xfe, 0x7c, 0x38, 0x10, 0x00},    /* Ascii 31 */
		{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},    /* */
		{0x30, 0x30, 0x30, 0x30, 0x30, 0x00, 0x30, 0x00},    /* ! */
		{0x66, 0x66, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00},    /* " */
		{0x6c, 0x6c, 0xfe, 0x6c, 0xfe, 0x6c, 0x6c, 0x00},    /* # */
		{0x10, 0x7c, 0xd2, 0x7c, 0x86, 0x7c, 0x10, 0x00},    /* $ */
		{0xf0, 0x96, 0xfc, 0x18, 0x3e, 0x72, 0xde, 0x00},    /* % */
		{0x30, 0x48, 0x30, 0x78, 0xce, 0xcc, 0x78, 0x00},    /* & */
		{0x0c, 0x0c, 0x18, 0x00, 0x00, 0x00, 0x00, 0x00},    /* ' */
		{0x10, 0x60, 0xc0, 0xc0, 0xc0, 0x60, 0x10, 0x00},    /* ( */
		{0x10, 0x0c, 0x06, 0x06, 0x06, 0x0c, 0x10, 0x00},    /* ) */
		{0x00, 0x54, 0x38, 0xfe, 0x38, 0x54, 0x00, 0x00},    /* * */
		{0x00, 0x18, 0x18, 0x7e, 0x18, 0x18, 0x00, 0x00},    /* + */
		{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x70},    /* , */
		{0x00, 0x00, 0x00, 0x7e, 0x00, 0x00, 0x00, 0x00},    /* - */
		{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x00},    /* . */
		{0x02, 0x06, 0x0c, 0x18, 0x30, 0x60, 0xc0, 0x00},    /* / */
		{0x7c, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x00},    /* 0 */
		{0x18, 0x38, 0x78, 0x18, 0x18, 0x18, 0x3c, 0x00},    /* 1 */
		{0x7c, 0xc6, 0x06, 0x0c, 0x30, 0x60, 0xfe, 0x00},    /* 2 */
		{0x7c, 0xc6, 0x06, 0x3c, 0x06, 0xc6, 0x7c, 0x00},    /* 3 */
		{0x0e, 0x1e, 0x36, 0x66, 0xfe, 0x06, 0x06, 0x00},    /* 4 */
		{0xfe, 0xc0, 0xc0, 0xfc, 0x06, 0x06, 0xfc, 0x00},    /* 5 */
		{0x7c, 0xc6, 0xc0, 0xfc, 0xc6, 0xc6, 0x7c, 0x00},    /* 6 */
		{0xfe, 0x06, 0x0c, 0x18, 0x30, 0x60, 0x60, 0x00},    /* 7 */
		{0x7c, 0xc6, 0xc6, 0x7c, 0xc6, 0xc6, 0x7c, 0x00},    /* 8 */
		{0x7c, 0xc6, 0xc6, 0x7e, 0x06, 0xc6, 0x7c, 0x00},    /* 9 */
		{0x00, 0x30, 0x00, 0x00, 0x00, 0x30, 0x00, 0x00},    /* : */
		{0x00, 0x30, 0x00, 0x00, 0x00, 0x30, 0x20, 0x00},    /* }, */
		{0x00, 0x1c, 0x30, 0x60, 0x30, 0x1c, 0x00, 0x00},    /* < */
		{0x00, 0x00, 0x7e, 0x00, 0x7e, 0x00, 0x00, 0x00},    /* = */
		{0x00, 0x70, 0x18, 0x0c, 0x18, 0x70, 0x00, 0x00},    /* > */
		{0x7c, 0xc6, 0x0c, 0x18, 0x30, 0x00, 0x30, 0x00},    /* ? */
		{0x7c, 0x82, 0x9a, 0xaa, 0xaa, 0x9e, 0x7c, 0x00},    /* @ */
		{0x38, 0x6c, 0xc6, 0xc6, 0xfe, 0xc6, 0xc6, 0x00},    /* A */
		{0xfc, 0xc6, 0xc6, 0xfc, 0xc6, 0xc6, 0xfc, 0x00},    /* B */
		{0x7c, 0xc6, 0xc6, 0xc0, 0xc0, 0xc6, 0x7c, 0x00},    /* C */
		{0xf8, 0xcc, 0xc6, 0xc6, 0xc6, 0xcc, 0xf8, 0x00},    /* D */
		{0xfe, 0xc0, 0xc0, 0xfc, 0xc0, 0xc0, 0xfe, 0x00},    /* E */
		{0xfe, 0xc0, 0xc0, 0xfc, 0xc0, 0xc0, 0xc0, 0x00},    /* F */
		{0x7c, 0xc6, 0xc0, 0xce, 0xc6, 0xc6, 0x7e, 0x00},    /* G */
		{0xc6, 0xc6, 0xc6, 0xfe, 0xc6, 0xc6, 0xc6, 0x00},    /* H */
		{0x78, 0x30, 0x30, 0x30, 0x30, 0x30, 0x78, 0x00},    /* I */
		{0x1e, 0x06, 0x06, 0x06, 0xc6, 0xc6, 0x7c, 0x00},    /* J */
		{0xc6, 0xcc, 0xd8, 0xf0, 0xd8, 0xcc, 0xc6, 0x00},    /* K */
		{0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xc0, 0xfe, 0x00},    /* L */
		{0xc6, 0xee, 0xfe, 0xd6, 0xc6, 0xc6, 0xc6, 0x00},    /* M */
		{0xc6, 0xe6, 0xf6, 0xde, 0xce, 0xc6, 0xc6, 0x00},    /* N */
		{0x7c, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x00},    /* O */
		{0xfc, 0xc6, 0xc6, 0xfc, 0xc0, 0xc0, 0xc0, 0x00},    /* P */
		{0x7c, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x06},    /* Q */
		{0xfc, 0xc6, 0xc6, 0xfc, 0xc6, 0xc6, 0xc6, 0x00},    /* R */
		{0x78, 0xcc, 0x60, 0x30, 0x18, 0xcc, 0x78, 0x00},    /* S */
		{0xfc, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x00},    /* T */
		{0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x7c, 0x00},    /* U */
		{0xc6, 0xc6, 0xc6, 0xc6, 0xc6, 0x6c, 0x38, 0x00},    /* V */
		{0xc6, 0xc6, 0xc6, 0xd6, 0xfe, 0xee, 0xc6, 0x00},    /* W */
		{0xc6, 0xc6, 0x6c, 0x38, 0x6c, 0xc6, 0xc6, 0x00},    /* X */
		{0xc3, 0xc3, 0x66, 0x3c, 0x18, 0x18, 0x18, 0x00},    /* Y */
		{0xfe, 0x0c, 0x18, 0x30, 0x60, 0xc0, 0xfe, 0x00},    /* Z */
		{0x3c, 0x30, 0x30, 0x30, 0x30, 0x30, 0x3c, 0x00},    /* [ */
		{0xc0, 0x60, 0x30, 0x18, 0x0c, 0x06, 0x03, 0x00},    /* \ */
		{0x3c, 0x0c, 0x0c, 0x0c, 0x0c, 0x0c, 0x3c, 0x00},    /* ] */
		{0x00, 0x38, 0x6c, 0xc6, 0x00, 0x00, 0x00, 0x00},    /* ^ */
		{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xff},    /* _ */
		{0x30, 0x30, 0x18, 0x00, 0x00, 0x00, 0x00, 0x00},    /* ` */
		{0x00, 0x00, 0x7c, 0x06, 0x7e, 0xc6, 0x7e, 0x00},    /* a */
		{0xc0, 0xc0, 0xfc, 0xc6, 0xc6, 0xe6, 0xdc, 0x00},    /* b */
		{0x00, 0x00, 0x7c, 0xc6, 0xc0, 0xc0, 0x7e, 0x00},    /* c */
		{0x06, 0x06, 0x7e, 0xc6, 0xc6, 0xce, 0x76, 0x00},    /* d */
		{0x00, 0x00, 0x7c, 0xc6, 0xfe, 0xc0, 0x7e, 0x00},    /* e */
		{0x1e, 0x30, 0x7c, 0x30, 0x30, 0x30, 0x30, 0x00},    /* f */
		{0x00, 0x00, 0x7e, 0xc6, 0xce, 0x76, 0x06, 0x7c},    /* g */
		{0xc0, 0xc0, 0xfc, 0xc6, 0xc6, 0xc6, 0xc6, 0x00},    /* */
		{0x18, 0x00, 0x38, 0x18, 0x18, 0x18, 0x3c, 0x00},    /* i */
		{0x18, 0x00, 0x38, 0x18, 0x18, 0x18, 0x18, 0xf0},    /* j */
		{0xc0, 0xc0, 0xcc, 0xd8, 0xf0, 0xd8, 0xcc, 0x00},    /* k */
		{0x38, 0x18, 0x18, 0x18, 0x18, 0x18, 0x3c, 0x00},    /* l */
		{0x00, 0x00, 0xcc, 0xfe, 0xd6, 0xc6, 0xc6, 0x00},    /* m */
		{0x00, 0x00, 0xfc, 0xc6, 0xc6, 0xc6, 0xc6, 0x00},    /* n */
		{0x00, 0x00, 0x7c, 0xc6, 0xc6, 0xc6, 0x7c, 0x00},    /* o */
		{0x00, 0x00, 0xfc, 0xc6, 0xc6, 0xe6, 0xdc, 0xc0},    /* p */
		{0x00, 0x00, 0x7e, 0xc6, 0xc6, 0xce, 0x76, 0x06},    /* q */
		{0x00, 0x00, 0x6e, 0x70, 0x60, 0x60, 0x60, 0x00},    /* r */
		{0x00, 0x00, 0x7c, 0xc0, 0x7c, 0x06, 0xfc, 0x00},    /* s */
		{0x30, 0x30, 0x7c, 0x30, 0x30, 0x30, 0x1c, 0x00},    /* t */
		{0x00, 0x00, 0xc6, 0xc6, 0xc6, 0xc6, 0x7e, 0x00},    /* u */
		{0x00, 0x00, 0xc6, 0xc6, 0xc6, 0x6c, 0x38, 0x00},    /* v */
		{0x00, 0x00, 0xc6, 0xc6, 0xd6, 0xfe, 0x6c, 0x00},    /* w */
		{0x00, 0x00, 0xc6, 0x6c, 0x38, 0x6c, 0xc6, 0x00},    /* x */
		{0x00, 0x00, 0xc6, 0xc6, 0xce, 0x76, 0x06, 0x7c},    /* y */
		{0x00, 0x00, 0xfc, 0x18, 0x30, 0x60, 0xfc, 0x00},    /* z */
		{0x0e, 0x18, 0x18, 0x70, 0x18, 0x18, 0x0e, 0x00},    /* { */
		{0x18, 0x18, 0x18, 0x00, 0x18, 0x18, 0x18, 0x00},    /* | */
		{0xe0, 0x30, 0x30, 0x1c, 0x30, 0x30, 0xe0, 0x00},    /* } */
		{0x00, 0x00, 0x70, 0x9a, 0x0e, 0x00, 0x00, 0x00},    /* ~ */
		{0x00, 0x00, 0x18, 0x3c, 0x66, 0xff, 0x00, 0x00}    /* Ascii 127 */
	};
	
	public static class EndianDataInputStream extends InputStream implements DataInput
	{
		DataInputStream dataIn;
		private ByteBuffer buffer = ByteBuffer.allocate(8);
		ByteOrder order = ByteOrder.BIG_ENDIAN;
		
		public EndianDataInputStream( InputStream stream ){
			dataIn = new DataInputStream( stream );
		}
		
		public EndianDataInputStream order(ByteOrder o){
			order = o;
			return this;
		}
		
		@Override
		public int read(byte[] b) throws IOException {
			return dataIn.read(b);
		}
		
		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			return dataIn.read(b, off, len);
		}
		
		@Deprecated
		@Override
		public String readLine() throws IOException {
			return dataIn.readLine();
		}
		
		@Override
		public boolean readBoolean() throws IOException {
			return dataIn.readBoolean();
		}
		
		@Override
		public byte readByte() throws IOException {
			return dataIn.readByte();
		}
		
		@Override
		public int read() throws IOException {
			return readByte();
		}
		
		@Override
		public boolean markSupported(){
			return dataIn.markSupported();
		}
		
		@Override
		public void mark(int readlimit) {
			dataIn.mark(readlimit);
		}
		
		@Override
		public void reset() throws IOException {
			dataIn.reset();
		}
		
		@Override
		public char readChar() throws IOException {
			return dataIn.readChar();
		}
		
		@Override
		public void readFully(byte[] b) throws IOException {
			dataIn.readFully(b);
		}
		
		@Override
		public void readFully(byte[] b, int off, int len) throws IOException {
			dataIn.readFully(b, off, len);
		}
		
		@Override
		public String readUTF() throws IOException {
			return dataIn.readUTF();
		}
		
		@Override
		public int skipBytes(int n) throws IOException {
			return dataIn.skipBytes(n);
		}
		
		@Override
		public double readDouble() throws IOException {
			long tmp = readLong();
			return Double.longBitsToDouble( tmp );
		}
		
		@Override
		public float readFloat() throws IOException {
			int tmp = readInt();
			return Float.intBitsToFloat( tmp );
		}
		
		@Override
		public int readInt() throws IOException {
			buffer.clear();
			buffer.order( ByteOrder.BIG_ENDIAN )
					.putInt( dataIn.readInt() )
					.flip();
			return buffer.order( order ).getInt();
		}
		
		@Override
		public long readLong() throws IOException {
			buffer.clear();
			buffer.order( ByteOrder.BIG_ENDIAN )
					.putLong( dataIn.readLong() )
					.flip();
			return buffer.order( order ).getLong();
		}
		
		@Override
		public short readShort() throws IOException {
			buffer.clear();
			buffer.order( ByteOrder.BIG_ENDIAN )
					.putShort( dataIn.readShort () )
					.flip();
			return buffer.order( order ).getShort();
		}
		
		@Override
		public int readUnsignedByte() throws IOException {
			return (int)dataIn.readByte();
		}
		
		@Override
		public int readUnsignedShort() throws IOException {
			return (int)readShort();
		}
	}
}
