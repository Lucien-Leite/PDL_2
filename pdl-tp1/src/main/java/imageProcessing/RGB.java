package imageProcessing;

import net.imglib2.Cursor;
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
import net.imglib2.view.IntervalView;
import net.imglib2.loops.LoopBuilder;

public class RGB {

    public static void blueFilter (Img<UnsignedByteType> input) {
        final RandomAccess<UnsignedByteType> r = input.randomAccess();
        final int iw = (int) input.max(0);
        final int ih = (int) input.max(1);

        // les valeurs sur les canaux rouge et vert sont mises à 0
        for (int channel = 0; channel <= 1; channel++) {
            for (int y = 0; y <= ih; ++y) {
                for (int x = 0; x <= iw; ++x) {
                    r.setPosition(x, 0);
                    r.setPosition(y, 1);
                    r.setPosition(channel, 2);
                    r.get().set(0);
                }
            }
        }
    }
    public static void lightChangeRgb (Img<UnsignedByteType> input,int delta){
        final Cursor<UnsignedByteType> cursor = input.localizingCursor();

        int tmp = 0;

        while (cursor.hasNext()) {
            cursor.fwd();
            int channel = cursor.getIntPosition(2);
            if (channel == 0 || channel == 1 || channel == 2)

            tmp = cursor.get().get();
            if (tmp >= 127){
                tmp = 127;
            }

            cursor.get().set(tmp+delta);
        }

    }

    public static void toGrayLevel(Img<UnsignedByteType> input){
        final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
        final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);

        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        final Cursor<UnsignedByteType> cB = inputB.cursor();

        int tmpR = 0;
        int tmpG = 0;
        int tmpB = 0;

        while (cR.hasNext() && cG.hasNext()) {
            cR.fwd();
            cG.fwd();
            cB.fwd();

            tmpR = cR.get().get();
            tmpR = (int) (tmpR*0.3+tmpG*0.59+tmpB*0.11);

            tmpG = cG.get().get();
            tmpG = (int) (tmpR*0.3+tmpG*0.59+tmpB*0.11);

            tmpB = cB.get().get();
            tmpB = (int) (tmpR*0.3+tmpG*0.59+tmpB*0.11);


            cR.get().set(tmpR);
            cG.get().set(tmpG);
            cB.get().set(tmpB);
        }
    }

    public static void blueFilterWithCursor (Img<UnsignedByteType> input) {
        final Cursor<UnsignedByteType> cursor = input.localizingCursor();

        while (cursor.hasNext()) {
            cursor.fwd();
            int channel = cursor.getIntPosition(2);
            if (channel == 0 || channel == 1)
                cursor.get().set(0);
        }
    }

    public static void blueFilterWithRGCursors(Img<UnsignedByteType> input) {
        final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);
       // final IntervalView<UnsignedByteType> inputB = Views.hyperSlice(input, 2, 2);

        final Cursor<UnsignedByteType> cR = inputR.cursor();
        final Cursor<UnsignedByteType> cG = inputG.cursor();
        //final Cursor<UnsignedByteType> cB = inputB.cursor();

        while (cR.hasNext() && cG.hasNext()) {
            cR.fwd();
            cG.fwd();
           // cB.fwd();

            cR.get().set(0);
            cG.get().set(0);
            //cB.get().set(0);
        }
    }


    public static void blueFilterWithLoopBuilder(Img<UnsignedByteType> input) {
        final IntervalView<UnsignedByteType> inputR = Views.hyperSlice(input, 2, 0);
        final IntervalView<UnsignedByteType> inputG = Views.hyperSlice(input, 2, 1);


        LoopBuilder.setImages(inputR, inputG).forEachPixel(
                (r, g) -> {
                    r.set(0);
                    g.set(0);
                }
        );
    }

    public static void main(final String[] args) throws ImgIOException, IncompatibleTypeException {
        // load image
        if (args.length < 2) {
            System.out.println("missing input or output image filename");
            System.exit(-1);
        }
        final String filename = args[0];
        final ArrayImgFactory<UnsignedByteType> factory = new ArrayImgFactory<>(new UnsignedByteType());
        final Img<UnsignedByteType> input = (Img<UnsignedByteType>) new ImgOpener().openImgs(filename, factory).get(0);

        // keep blue channel
        //blueFilter(input);
        //blueFilterWithCursor(input);
        //blueFilterWithRGCursors(input);
        //blueFilterWithLoopBuilder(input);
        //lightChangeRgb(input,10);
        toGrayLevel(input);


        // save output image
        // Clear the file if it already exists.
        final String outPath = args[1];
        File path = new File(outPath);
        if (path.exists()) {
            path.delete();
        }
        ImgSaver imgSaver = new ImgSaver();
        imgSaver.saveImg(outPath, input);
        System.out.println("Image saved in: " + outPath);
    }
}
