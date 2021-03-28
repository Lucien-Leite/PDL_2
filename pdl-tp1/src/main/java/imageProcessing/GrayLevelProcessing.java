package imageProcessing;

import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import io.scif.SCIFIO;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
//import jdk.internal.org.jline.terminal.Cursor;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.exception.IncompatibleTypeException;
import java.io.File;
import net.imglib2.Cursor;

public class GrayLevelProcessing{

	public static void threshold(Img<UnsignedByteType> img, int t) {
		final RandomAccess<UnsignedByteType> r = img.randomAccess();

		final int iw = (int) img.max(0);
		final int ih = (int) img.max(1);

		for (int x = 0; x <= iw; ++x) {
			for (int y = 0; y <= ih; ++y) {
				r.setPosition(x, 0);
				r.setPosition(y, 1);
				final UnsignedByteType val = r.get();
				if (val.get() < t)
				    val.set(0);
				else
				    val.set(255);
			}
		}

	}

	public static void lightChange(Img<UnsignedByteType> img, int delta){
		final RandomAccess<UnsignedByteType> r = img.randomAccess();
		
		final int iw = (int) img.max(0);
		final int ih = (int) img.max(1);
		
		int tmp;

		for (int x = 0; x <= iw; ++x) {
			for (int y = 0; y <= ih; ++y) {
				r.setPosition(x, 0);
				r.setPosition(y, 1);
				final UnsignedByteType val = r.get();
				tmp = val.get();
				tmp=tmp+delta;
				if(tmp > 255){
					tmp = 255;
				} 
				val.set(tmp);
			}
		}

	}

	public static void lightChangeCur(Img<UnsignedByteType> img, int delta){
		final Cursor < UnsignedByteType > cursor = img.cursor();
		int tmp;
		while ( cursor.hasNext() ){
			cursor.fwd();
			final UnsignedByteType t = cursor.get();
			tmp = t.get();
			tmp=tmp+delta;
			if(tmp > 255){
				tmp = 255;
			} 
			t.set(tmp);
		}

	}
	
	public static void contrastChange(Img<UnsignedByteType> img, int min, int max){
		
		final Cursor < UnsignedByteType > cursor = img.cursor();
		int imin = 255;
		int imax = 0;
		int ip = 0;
		while ( cursor.hasNext() ){
			cursor.fwd();
			final UnsignedByteType t = cursor.get();
			if(imin>t.get()){
				imin = t.get();
			}
			if(imax<t.get()){
				imax = t.get();
			}
		}

		cursor.reset();
		
		while ( cursor.hasNext() ){
			cursor.fwd();
			final UnsignedByteType t = cursor.get();
			
			ip = ((t.get()-imin)/(imax-imin))*(max-min);
		}
	}




	public static void main(final String[] args) throws ImgIOException, IncompatibleTypeException {
		// load image
		if (args.length < 2) {
			System.out.println("missing input and/or output image filenames");
			System.exit(-1);
		} 
		final String filename = args[0];
		if (!new File(filename).exists()) {
			System.err.println("File '" + filename + "' does not exist");
			System.exit(-1);
		}

		final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
		final ImgOpener imgOpener = new ImgOpener();
		final Img<UnsignedByteType> input = (Img<UnsignedByteType>) imgOpener.openImgs(filename, factory).get(0);
		imgOpener.context().dispose();

		// process image
		//threshold(input, 128);
		lightChange(input, 100);
		//lightChangeCur(input, 100);
		//contrastChange(input,255,0);



		// save output image
		final String outPath = args[1];
		File path = new File(outPath);
		// clear the file if it already exists.
		if (path.exists()) {
			path.delete();
		}
		ImgSaver imgSaver = new ImgSaver();
		imgSaver.saveImg(outPath, input);
		imgSaver.context().dispose();
		System.out.println("Image saved in: " + outPath);		
	}

}
