package com.example.daniel.fundowngame;

import android.annotation.TargetApi;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Timer;
import java.util.TimerTask;

public class Pantalla2 extends AppCompatActivity {

    private static  int eggframe_w=368,Actualframe_w=0;
    private static  int eggframe_h=594,Actualframe_h=0;
    private static  int eggnum_frames=3,Actualnum_f=0;
    private static  int eggcount_x=3,ActualCount_x=0;
    private static  int count_y=1;
    private static  int frame_duration=220;
    private static  int scale_factor=5;

    float posx=0,posy=0;
    ImageView img;
    Bitmap[] btm;
    Bitmap eggs,kirby,moneda,btmActual;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla2);
        findViewById(R.id.huevoimg).setOnLongClickListener(new MyOnLongClickListener(false));

        findViewById(R.id.contenedor).setOnDragListener(new MyOnDragListener(1, false));
        findViewById(R.id.taza).setOnDragListener(new MyOnDragListener(2, false));
        findViewById(R.id.dropos).setOnDragListener(new MyOnDragListener(3, false));
        findViewById(R.id.huevo).setOnDragListener(new MyOnDragListener(4, false));

        eggs=getBitmapFromAssets(this,"huevosmodif.png");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pantalla2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Bitmap getBitmapFromAssets(Pantalla2 mainActivity, String s) {
        AssetManager assetM = mainActivity.getAssets();
        InputStream istr = null;
        Bitmap bitmap = null;

        try {
            istr = assetM.open(s);
            bitmap = BitmapFactory.decodeStream(istr);
        } catch (IOException ioe) {
            // manage exception
        } finally {
            if (istr != null) {
                try {
                    istr.close();
                } catch (IOException e) {
                }
            }
        }

        return bitmap;
    }

    class MyOnLongClickListener implements View.OnLongClickListener {
        boolean visible;
        public MyOnLongClickListener(boolean visible) {
            this.visible=visible;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public boolean onLongClick(View v) {
            img=(ImageView) findViewById(v.getId());
            posx=v.getX(); posy=v.getY(); Log.i("posxy", posx + " " + posy);
            ClipData data = ClipData.newPlainText("simple_text","text");
            View.DragShadowBuilder sb = new View.DragShadowBuilder(v);

            v.startDrag(data, sb, v, 0);
            if(visible==false){
                v.setVisibility(View.INVISIBLE);}else{v.setVisibility(View.VISIBLE);}

            return true;
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    class MyOnDragListener implements View.OnDragListener{
        private int num;
        boolean VerifDrop,dropp=false;
        int ActualNum=0;
        public MyOnDragListener(int num, boolean VerifDrop) {
            super();
            this.num=num;
            this.VerifDrop=VerifDrop;
        }

        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();


            switch (action){
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i("Script",num+" - ACTION_DRAG_STARTED");
                    if(event.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)){return true;}else{return false;}

                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i("Script",num+" - ACTION_DRAG_ENTERED");
                    ActualNum=num;
                    //if(num!=1){v.setBackgroundColor(Color.BLUE);}

                    break;
                case DragEvent.ACTION_DRAG_LOCATION:
                    Log.i("Script", num + " - ACTION_DRAG_LOCATION " + event.getX() + " " + event.getY());
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i("Script",num+" - ACTION_DRAG_EXITED");
                    if(num==1){
                        Log.i("Script", num + " - drop fuera de limite");
                        img.setVisibility(View.VISIBLE);}

                    break;
                case DragEvent.ACTION_DROP:
                    Log.i("Script",num+" - ACTION_DRAG_DROP");
                    VerifDrop=true;
                    if(num!=1){
                        View view = (View) event.getLocalState();
                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);
                        LinearLayout container = (LinearLayout)  findViewById(R.id.dropos);
                        container.addView(view);
                        view.setVisibility(View.VISIBLE);

                        //** Animacion +++++++++++++++++++
                        if (img.getId() == R.id.huevoimg) {
                            btmActual = eggs;
                            Actualnum_f = eggnum_frames;
                            ActualCount_x = eggcount_x;
                            Actualframe_w=eggframe_w;
                            Actualframe_h=eggframe_h;
                        }

                        // Bitmap eggs=getBitmapFromAssets(this,"bigeggsprites.png");

                        if (btmActual != null) {
                            // cut bitmaps from bird bmp to array of bitmaps
                            btm = new Bitmap[Actualnum_f];
                            int currentFrame = 0;

                            for (int i = 0; i < count_y; i++) {
                                for (int j = 0; j < ActualCount_x; j++) {
                                    btm[currentFrame] = Bitmap.createBitmap(btmActual, Actualframe_w
                                            * j, Actualframe_h * i, Actualframe_w, Actualframe_h);

                                    // apply scale factor
                                    btm[currentFrame] = Bitmap.createScaledBitmap(
                                            btm[currentFrame], Actualframe_w * scale_factor, Actualframe_h
                                                    * scale_factor, true);

                                    if (++currentFrame >= Actualnum_f) {
                                        break;
                                    }
                                }
                            }

                            // create animation programmatically
                            final AnimationDrawable animation = new AnimationDrawable();
                            animation.setOneShot(false); // repeat animation

                            for (int i = 0; i < Actualnum_f; i++) {
                                animation.addFrame(new BitmapDrawable(getResources(), btm[i]),
                                        frame_duration);
                            }

                            // load animation on image
                            if (Build.VERSION.SDK_INT < 16) {
                                img.setBackgroundDrawable(animation);
                            } else {
                                img.setBackground(animation);
                            }


                            // start animation on image
                            img.post(new Runnable() {

                                @Override
                                public void run() {
                                    animation.start();
                                    long totalDuration = 0;
                                    for (int i = 0; i < animation.getNumberOfFrames(); i++) {
                                        totalDuration += animation.getDuration(i);
                                    }
                                    Timer timer = new Timer();
                                    TimerTask timerTask = new TimerTask() {
                                        @Override
                                        public void run() {
                                            animation.stop();
                                        }
                                    };
                                    timer.schedule(timerTask, totalDuration);


                                }

                            });

                        }
                        //***
                    }else{
                        img.setVisibility(View.VISIBLE);
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i("Script",num+" - ACTION_DRAG_ENDED");

                    break;
            }
            return true;
        }
    }
}
