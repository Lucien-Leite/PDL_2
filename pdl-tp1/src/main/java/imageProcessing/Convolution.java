package imageProcessing;

import net.imglib2.Cursor;
import net.imglib2.Dimensions;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import io.scif.img.ImgIOException;
import io.scif.img.ImgOpener;
import io.scif.img.ImgSaver;
import net.imglib2.type.numeric.integer.UnsignedByteType;
import net.imglib2.exception.IncompatibleTypeException;
import java.io.File;
import net.imglib2.view.Views;
import net.imglib2.view.ExtendedRandomAccessibleInterval;
import net.imglib2.view.IntervalView;
import net.imglib2.algorithm.gauss3.Gauss3;
import net.imglib2.RandomAccessibleInterval;
import net.imglib2.algorithm.neighborhood.RectangleShape;
import net.imglib2.algorithm.neighborhood.Neighborhood;
import net.imglib2.util.Intervals;
import net.imglib2.Interval;

public class Convolution {

	/**
	 * Question 1.1 essayer de faire les modifs directement sur l'output au lieu de le faire sur le tableau
	 */
	public static void meanFilterSimple(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {

		final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendZero(input);
		final RandomAccess<UnsignedByteType> in = extendedView.randomAccess();
		final long[] dimensions = new long[2];
		input.dimensions(dimensions);
		System.out.println(" ICI BORDEL \n" +
				"LA JUSTE ICI\n" +
				"LAAAAAAAA OOOOOOH MEEEEERDE\n" +
				"C'EST BON TU M'A VUS CONNARD\n");

		int[][] copy;
		copy = new int[(int) dimensions[0]][(int) dimensions[1]];

		int[][] k =
				{
						{0,1,0},
						{1,-4,1},
						{0,1,0}
				};
		int n = 3;
		//k = new int[n][n];

		/*for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				k[i][j] = 1*(1/9);
			}
		}*/



		for (int i = 0; i < dimensions[0]; i++) {
			for (int j = 0; j < dimensions[1]; j++) {

				in.setPosition(i, 0);
				in.setPosition(j, 1);
				copy[i][j] = in.get().get();

			}
		}

		int r = 0;
		int px = n/2;
		int py = n/2;

		for (int i = 1; i < (dimensions[0]-1); i++) {
			for (int j = 1; j < (dimensions[1]-1); j++) {
				r=0;
				for (int u = -px; u <= px; u++) {
					for (int v = -py; v <= py; v++) {

						r = r + copy[i+u][j+v] * k[u+px][v+py];
						copy[i][j]=r;

					}
				}
			}
		}
		final RandomAccess<UnsignedByteType> out = output.randomAccess();

		final int iw = (int) output.max(0);
		final int ih = (int) output.max(1);

		for (int i = 0; i < iw; i++) {
			for (int j = 0; j < ih; j++) {
				out.setPosition(i, 0);
				out.setPosition(j, 1);
				final UnsignedByteType val = out.get();
				val.set(copy[i][j]);
			}
		}
	}

	/**
	 * Question 1.2
	 */
	public static void meanFilterWithBorders(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
			int size) {
	}

	/**
	 * Question 1.3
	 */
	public static void meanFilterWithNeighborhood(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
			int size) {

	}

	/**
	 * Question 2.1
	 */
	public static void convolution(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output,
			int[][] kernel) {

	}

	/**
	 * Question 2.3
	 */
	public static void gaussFilterImgLib(final Img<UnsignedByteType> input, final Img<UnsignedByteType> output) {

    }
    
    public static void testExtend(Img<UnsignedByteType> input) {
        final ExtendedRandomAccessibleInterval<UnsignedByteType, Img<UnsignedByteType>> extendedView = Views.extendZero(input);
        final RandomAccess<UnsignedByteType> in = extendedView.randomAccess();
        final long[] dimensions = new long[2];
        input.dimensions(dimensions);
		System.out.println("Dimensions de l'image initiale : " + dimensions[0] + " x " + dimensions[1]);
		// extendedView.dimensions(dimensions); -> méthode non définie, on ne peut pas non plus utiliser un Cursor sur cette vue
            
        int x = 4000;
        int y = 4000;
        in.setPosition(x, 0);
        in.setPosition(y, 1);
        System.out.println("Valeur au point " + x + " " + y + " : " + in.get().get() );

	}
	
	public static void testExpand(Img<UnsignedByteType> input) {
		final IntervalView<UnsignedByteType> expandedView = Views.expandMirrorDouble(input, 1, 1 );
		
        final RandomAccess<UnsignedByteType> in = expandedView.randomAccess();
        final long[] dimensions = new long[2];
        input.dimensions(dimensions);
		System.out.println("Dimensions de l'image initiale : " + dimensions[0] + " x " + dimensions[1]);
		int x = 0;
		int y = (int) (dimensions[1]-1);

		expandedView.dimensions(dimensions);
        System.out.println("Dimensions de l'image expansée : " + dimensions[0] + " x " + dimensions[1]);
           
        
        in.setPosition(x, 0);
        in.setPosition(y, 1);
		System.out.println("Valeur au point " + x + " " + y + " : " + in.get().get() );
	
        y = y + 1;
        in.setPosition(x, 0);
        in.setPosition(y, 1);
		System.out.println("Valeur au point " + x + " " + y + " : " + in.get().get() );
	
	}
	
	public static void testSubPart(Img<UnsignedByteType> input) {
		RandomAccessibleInterval< UnsignedByteType > upperLeftPart =
				Views.interval( input, new long[] { 0, 0 }, new long[]{ 2, 2} );
		final Cursor< UnsignedByteType > c = Views.iterable( upperLeftPart ).cursor();
		while (c.hasNext()){
			c.fwd();
			System.out.print(c.get() + " ");
		}
		System.out.println();
	}

	public static void main(final String[] args) throws ImgIOException, IncompatibleTypeException {

		// load image
		if (args.length < 2) {
			System.out.println("missing input or output image filename");
			System.exit(-1);
		}
		final String filename = args[0];
		final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
		final ImgOpener imgOpener = new ImgOpener();
		final Img<UnsignedByteType> input = (Img<UnsignedByteType>) imgOpener.openImgs(filename, factory).get(0);
		imgOpener.context().dispose();

		// output image of same dimensions
		final Dimensions dim = input;
		final Img<UnsignedByteType> output = factory.create(dim);

		// mean filter
        meanFilterSimple(input, output);
        //testExtend(input);
		//testExpand(input);
		//testSubPart(input);

		final String outPath = args[1];
		File path = new File(outPath);
		if (path.exists()) {
			path.delete();
		}
		ImgSaver imgSaver = new ImgSaver();
		imgSaver.saveImg(outPath, output);
		imgSaver.context().dispose();
		System.out.println("Image saved in: " + outPath);
	}

}
