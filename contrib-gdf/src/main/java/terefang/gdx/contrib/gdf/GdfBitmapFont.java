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
import terefang.gdx.contrib.gdf.tables.Ascii6x11Table;
import terefang.gdx.contrib.gdf.tables.Ascii8x8Table;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.zip.GZIPInputStream;

public class GdfBitmapFont extends BitmapFont
{
	public static final BitmapFont create8x8() throws IOException
	{
		return create(null, "8x8");
	}
	
	public static final BitmapFont create6x11() throws IOException
	{
		return create(null, "6x11");
	}
	
	public static final BitmapFont create(FileHandleResolver resolver, String fileName) throws IOException
	{
		BitmapFontData data = new BitmapFontData();
		Gdx2DPixmap pixmap = null;
		InputStream ins = null;
		FileHandle res = null;
		if(resolver != null)
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
		
		if("8x8".equalsIgnoreCase(fileName))
		{
			pixmap = createFromInternal(data, Ascii8x8Table.table, Ascii8x8Table.start, Ascii8x8Table.table.length, Ascii8x8Table.table[0].length, Ascii8x8Table.width, Ascii8x8Table.wstep, Ascii8x8Table.hstep);
		}
		else if("6x11".equalsIgnoreCase(fileName))
		{
			pixmap = createFromInternal(data, Ascii6x11Table.table, Ascii6x11Table.start, Ascii6x11Table.table.length, Ascii6x11Table.table[0].length, Ascii6x11Table.width, Ascii6x11Table.wstep, Ascii6x11Table.hstep);
		}
		else if(fileName == null || ins == null)
		{
			pixmap = createFromInternal(data, Ascii6x11Table.table, Ascii6x11Table.start, Ascii6x11Table.table.length, Ascii6x11Table.table[0].length, Ascii6x11Table.width, Ascii6x11Table.wstep, Ascii6x11Table.hstep);
		}
		else if(fileName.endsWith(".gdfa"))
		{
			pixmap = createFromGDFA(data, ins);
		}
		else if(fileName.endsWith(".gdfa.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFA(data, ins);
		}
		else if(fileName.endsWith(".gdf"))
		{
			pixmap = createFromGDFB(data, ins, false);
		}
		else if(fileName.endsWith(".gdf.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFB(data, ins, false);
		}
		else if(fileName.endsWith(".gdfx"))
		{
			pixmap = createFromGDFB(data, ins, true);
		}
		else if(fileName.endsWith(".gdfx.gz"))
		{
			ins = new GZIPInputStream(ins);
			pixmap = createFromGDFB(data, ins, true);
		}
		else
		{
			throw new IllegalArgumentException("Unknown Font Format = " + fileName);
		}
		
		Texture texture = new Texture((TextureData) (new PixmapTextureData(new Pixmap(pixmap), (Pixmap.Format) null, false, true)));
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
		data.spaceWidth = (float) charW + 1;
		data.xHeight = (charH / 2) + 1;
		data.capHeight = charH;
		data.ascent = 0;
		data.down = -data.lineHeight;
		data.descent = 0;
		
		Glyph glyph;
		int ci = 0;
		for(int c = startChar; c < (numChars + startChar); c++)
		{
			glyph = new Glyph();
			glyph.id = c;
			if(charWadvance != null)
			{
				glyph.xadvance = charWadvance[ci];
			}
			else
			{
				glyph.xadvance = charW + 1;
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
	
	public static final Gdx2DPixmap createFromInternal(BitmapFontData data, int[][] asciiTable,
													   int startChar,
													   int numChars,
													   int charH,
													   int charW,
													   int charWstep,
													   int charHstep) throws IOException
	{
		
		Gdx2DPixmap pixmap = createPixmap(256, 256);
		
		setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, 16, null);
		
		for(int c = startChar; c < (startChar + numChars); c++)
		{
			Glyph glyph = data.getGlyph((char) c);
			int srcX = glyph.srcX;
			int srcY = glyph.srcY;
			for(int y = 0; y < charH; y++)
			{
				int mask = 0x80;
				for(int x = 0; x < charW; x++)
				{
					;
					pixmap.setPixel(srcX + x, srcY + y, ((asciiTable[c - startChar][y] & mask) == 0 ? 0 : 0xFFFFffff));
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
			int totalArea = nextPowerOf2(charHstep * charWstep * numpChars);
			int texRes = Integer.numberOfTrailingZeros(totalArea) + 1;
			texRes /= 2;
			texRes = 1 << texRes;
			int charPerRow = texRes / charWstep;
			
			pixmap = createPixmap(texRes, texRes);
			
			setBmData(data, startChar, numChars, charH, charW, charHstep, charWstep, charPerRow, null);
			
			int ci = 0;
			for(int c = startChar; c < (startChar + numChars); c++)
			{
				Glyph glyph = data.getGlyph((char) c);
				int srcX = glyph.srcX;
				int srcY = glyph.srcY;
				for(int y = 0; y < charH; y++)
				{
					for(int x = 0; x < charW; x++)
					{
						st.nextToken();
						pixmap.setPixel(srcX + x, srcY + y, (st.nval == 0 ? 0 : 0xFFFFffff));
					}
				}
				ci++;
			}
			ins.close();
		}
		else
		{
			throw new IllegalArgumentException("Unknown Font Format = " + st.sval);
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
			numChars = ((numChars >>> 24) & 0xff) | ((numChars >>> 8) & 0xff00);
		}
		
		int startChar = edis.readInt();
		int charW = edis.readInt();
		int charH = edis.readInt();
		
		int charWstep = nextPowerOf2(charW);
		int charHstep = nextPowerOf2(charH);
		
		int numpChars = nextPowerOf2(numChars);
		int totalArea = nextPowerOf2(charHstep * charWstep * numpChars);
		int texRes = Integer.numberOfTrailingZeros(totalArea) + 1;
		texRes /= 2;
		texRes = 1 << texRes;
		int charPerRow = texRes / charWstep;
		
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
		for(int c = startChar; c < (startChar + numChars); c++)
		{
			Glyph glyph = data.getGlyph((char) c);
			int srcX = glyph.srcX;
			int srcY = glyph.srcY;
			
			// ext format is grey-scale
			if(extendedFormat)
			{
				for(int y = 0; y < charH; y++)
				{
					for(int x = 0; x < charW; x++)
					{
						;
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
				for(int y = 0; y < charH; y++)
				{
					for(int x = 0; x < charW; x++)
					{
						int b = edis.readByte() & 0xff;
						pixmap.setPixel(srcX + x, srcY + y, (b == 0 ? 0 : 0xFFFFffff));
					}
				}
			}
			ci++;
		}
		edis.close();
		return pixmap;
	}
	
	public static final void dumpGDFB(InputStream ins) throws IOException
	{
		EndianDataInputStream edis = new EndianDataInputStream(ins);
		edis.order(ByteOrder.BIG_ENDIAN);
		int numChars = edis.readInt();
		if(numChars != (numChars & 0xffff))
		{
			edis.order(ByteOrder.LITTLE_ENDIAN);
			numChars = ((numChars >>> 24) & 0xff) | ((numChars >>> 8) & 0xff00);
		}
		
		int startChar = edis.readInt();
		int charW = edis.readInt();
		int charH = edis.readInt();
		
		int charWstep = nextPowerOf2(charW);
		int charHstep = nextPowerOf2(charH);
		
		System.out.println("// startChar=" + startChar);
		System.out.println("// numChar=" + numChars);
		System.out.println("// charW=" + charW);
		System.out.println("// charH=" + charH);
		
		int numpChars = nextPowerOf2(numChars);
		int totalArea = nextPowerOf2(charHstep * charWstep * numpChars);
		int texRes = Integer.numberOfTrailingZeros(totalArea) + 1;
		texRes /= 2;
		texRes = 1 << texRes;
		int charPerRow = texRes / charWstep;
		
		int ci = 0;
		for(int c = startChar; c < (startChar + numChars); c++)
		{
			System.out.print(" { ");
			for(int y = 0; y < charH; y++)
			{
				int pb = 0;
				for(int x = 0; x < charW; x++)
				{
					int b = edis.readByte() & 0xff;
					pb = (pb << 1) | (b == 0 ? 0 : 1);
				}
				System.out.print(String.format("0x%02X, ", pb));
			}
			System.out.println(" }, // chr=" + c);
			ci++;
		}
		edis.close();
	}
	
	public GdfBitmapFont(BitmapFontData data, TextureRegion region)
	{
		super(data, region, false);
	}
	

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
