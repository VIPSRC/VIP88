package pubgm.loader.floating;




import static pubgm.loader.activity.MainActivity.modeselect;
import static pubgm.loader.activity.MainActivity.typelogin;
import static pubgm.loader.floating.Overlay.getConfig;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.util.LruCache;
import android.view.Surface;
import android.view.View;
import android.graphics.Path;

import pubgm.loader.R;
import pubgm.loader.activity.MainActivity;


public class ESPView extends View implements Runnable {
    private Paint
    mStrokePaint,
    mFilledPaint,
    mFillPaint,
    mTextPaint,
    mTextPainti,
    mTexturePaint,
    mNamePaint,
    mFPSText,
    mMDText,
    mPaintBitmap,
    mPaintBitmap1,
    mItemsPaint,
	mVehiclesPaint,
    PaintTextBold,
    mLootBoxPaint;
    
    private Thread mThread;
    String selectmode = modeselect;
    String loginmode = typelogin;
    private  static int itemSize,itemPosition;

    Bitmap bitmap,out,botBitmap, lootBitmap, airdropBitmap, vehicleBitmap, bikeBitmap, kudaBitmap,boatBitmap,buggyBitmap,uazBitmap,trikeBitmap,daciaBitmap,jetBitmap,busBitmap,miradoBitmap,scooterBitmap,ronyBitmap,snowbikeBitmap,snowmobileBitmap,tempoBitmap,monsterBitmap,brdmBitmap,atvBitmap,truckBitmap,gliderBitmap,utvBitmap,coupeBitmap,airplaneBitmap;
    public static long sleepTime;
    private float    mFPS = 0.0f;
    private float    mFPSCounter = 0.0f;
    private long   mFPSTime = 0;
    private float mScaleX = 1;
    private float mScaleY = 1;
    private static LruCache<Integer, Bitmap> bitmapCache = new LruCache<>(10 * 1024 * 1024);
    Path path = new Path();

    public static void ChangeFps(int fps) {
        sleepTime = 1000 / fps;
    }

    Paint p,p2;
    Bitmap[] OTHER = new Bitmap[7]; //dihitung dari 0

    private static final int[] OTH_NAME = {
        R.drawable.ic_clear_enemy,
		R.drawable.ic_clear_boot,
		R.drawable.ic_danger_enemy,
		R.drawable.ic_danger_boot,
            R.drawable.ic_warning,
            R.drawable.ic_boot,
            R.drawable.ic_alert
    };

    public ESPView(Context context) {
        super(context, null, 0);
        InitializePaints();
        setFocusableInTouchMode(false);
        setBackgroundColor(Color.TRANSPARENT);
        mThread = new Thread(this);
        mThread.start();
    }
    

    @Override
    protected void onDraw(Canvas canvas) {
        int rotation = getDisplay().getRotation();
        if (canvas == null || rotation == Surface.ROTATION_0 || rotation == Surface.ROTATION_180) {
            return;
        }
        ClearCanvas(canvas);
        Overlay.DrawOn(this, canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mScaleX = getWidth() / (float) 2340;
        mScaleY = getHeight() / (float) 1080;
        botBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_bot), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        kudaBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.reindeer), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        lootBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lootx), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        airdropBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.airdrop), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        vehicleBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.vehicle), (int) (50 * mScaleY) , (int) (50 * mScaleY), false);
       buggyBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve0), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       uazBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve1), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       trikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve2), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       bikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve3), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       daciaBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve4), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       jetBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve5), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       boatBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve6), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       busBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve7), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       miradoBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve8), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       scooterBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve9), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       ronyBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve10), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       snowbikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve11), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       snowmobileBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve12), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       tempoBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve13), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       monsterBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve14), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       brdmBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve15), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       atvBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve18), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       truckBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve19), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       gliderBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve20), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       utvBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve21), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       coupeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve22), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       airplaneBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve23), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
    }

    @Override
    public void run() {
        while (mThread.isAlive() && !mThread.isInterrupted()) {
            try {
                long t1 = System.currentTimeMillis();
                postInvalidate();
                long td = System.currentTimeMillis() - t1;

                Thread.sleep(Math.max(Math.min(0, sleepTime - td), sleepTime));
            } catch (Exception e) {
                Thread.currentThread().interrupt();
                return;              
            }
        }
    }
    
    
    public void InitializePaints() {
        
        botBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_bot), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        kudaBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.reindeer), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        lootBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.lootx), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        airdropBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.airdrop), (int) (50 * mScaleY), (int) (50 * mScaleY), false);
        vehicleBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.vehicle), (int) (50 * mScaleY) , (int) (50 * mScaleY), false);

       buggyBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve0), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       uazBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve1), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       trikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve2), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       bikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve3), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       daciaBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve4), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       jetBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve5), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       boatBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve6), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       busBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve7), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       miradoBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve8), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       scooterBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve9), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       ronyBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve10), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       snowbikeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve11), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       snowmobileBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve12), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       tempoBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve13), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       monsterBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve14), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       brdmBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve15), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       atvBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve18), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       truckBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve19), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       gliderBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve20), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       utvBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve21), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       coupeBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve22), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
       airplaneBitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ve23), (int) (40 * mScaleY), (int) (40 * mScaleY), false);
        
        mPaintBitmap = new Paint();
        mPaintBitmap.setAlpha(225);
        
        mPaintBitmap1 = new Paint();
        mPaintBitmap1.setAlpha(255);
        
        mVehiclesPaint = new Paint();
        mVehiclesPaint.setAntiAlias(true);
        mVehiclesPaint.setTextAlign(Paint.Align.CENTER);
        mVehiclesPaint.setTypeface(getResources().getFont(R.font.mfontx));
        
        mItemsPaint = new Paint();
        mItemsPaint.setAntiAlias(true);
        mItemsPaint.setTextAlign(Paint.Align.CENTER);
        mItemsPaint.setTypeface(getResources().getFont(R.font.mfontx));
        
        mStrokePaint = new Paint();
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(Color.rgb(0, 0, 0));
        mStrokePaint.setTextAlign(Paint.Align.CENTER);

        mFilledPaint = new Paint();
        mFilledPaint.setStyle(Paint.Style.FILL);
        mFilledPaint.setAntiAlias(true);       
        mFilledPaint.setColor(Color.rgb(0, 0, 0));
        mFilledPaint.setStrokeWidth(3.0f);
        
        mFillPaint = new Paint();
        mFillPaint.setStyle(Paint.Style.FILL);
        mFillPaint.setAntiAlias(true);       
        mFillPaint.setColor(Color.rgb(0, 0, 0));
        
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.rgb(0, 0, 0));
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setShadowLayer(3, 2, 2, Color.BLACK);
        mTextPaint.setStrokeWidth(1.1f);
        
        mTextPainti = new Paint();
        mTextPainti.setStyle(Paint.Style.FILL);
        mTextPainti.setAntiAlias(true);
        mTextPainti.setColor(Color.rgb(0, 0, 0));
        mTextPainti.setTextAlign(Paint.Align.CENTER);
        
        
        mLootBoxPaint = new Paint();
        mLootBoxPaint.setAntiAlias(true);
        mLootBoxPaint.setTextAlign(Paint.Align.LEFT);
        mLootBoxPaint.setColor(Color.rgb(0, 0, 0));
        mLootBoxPaint.setTypeface(getResources().getFont(R.font.mfontx));
        mLootBoxPaint.setDither(true);
        
        mTexturePaint = new Paint();
        mTexturePaint.setStyle(Paint.Style.FILL);
        mTexturePaint.setAntiAlias(true);
        mTexturePaint.setColor(Color.rgb(0, 0, 0));
        mStrokePaint.setStrokeWidth(0.5f);
        mTexturePaint.setTextAlign(Paint.Align.CENTER);
        mTexturePaint.setShadowLayer(10, 1, 1, Color.rgb(1,1,1));
        mTexturePaint.setTypeface(getResources().getFont(R.font.cof));


        mNamePaint = new Paint();
        mNamePaint.setStyle(Paint.Style.FILL);
        mNamePaint.setAntiAlias(true);
        mNamePaint.setColor(Color.rgb(0, 0, 0));
        mNamePaint.setTextAlign(Paint.Align.CENTER);
        mNamePaint.setTextAlign(Paint.Align.CENTER);
       // mNamePaint.setShadowLayer(10, 1, 1, Color.rgb(1,1,1));
        mNamePaint.setTypeface(getResources().getFont(R.font.mfontx));

        mFPSText = new Paint();
        mFPSText.setStyle(Paint.Style.FILL_AND_STROKE);
        mFPSText.setAntiAlias(true);
        mFPSText.setColor(Color.rgb(255, 255, 0));
        mStrokePaint.setStrokeWidth(0.5f);
        mFPSText.setTextAlign(Paint.Align.CENTER);
        mFPSText.setShadowLayer(10, 1, 1, Color.rgb(1,1,1));
        mFPSText.setTypeface(getResources().getFont(R.font.mfontx));

        mMDText = new Paint();
        mMDText.setStyle(Paint.Style.FILL_AND_STROKE);
        mMDText.setAntiAlias(true);
        mMDText.setColor(Color.rgb(0, 0, 0));
        mMDText.setStrokeWidth(0.5f);
        mMDText.setTextAlign(Paint.Align.CENTER);
        mMDText.setShadowLayer(10, 1, 1, Color.rgb(1,1,1));
        
        PaintTextBold = new Paint();
        PaintTextBold.setStyle(Paint.Style.FILL_AND_STROKE);
        PaintTextBold.setAntiAlias(true);
        PaintTextBold.setColor(Color.rgb(0, 0, 0));
        PaintTextBold.setTextAlign(Paint.Align.CENTER);
        PaintTextBold.setStrokeWidth(0.2f);
        PaintTextBold.setShadowLayer(10, 1, 1, Color.BLACK);
        PaintTextBold.setTypeface(getResources().getFont(R.font.mfontx));
        
        

        p2=new Paint();
        final int bitmap_count_oth = OTHER.length;
        for (int i = 0 ; i < bitmap_count_oth ; i++) {
            OTHER[i] = BitmapFactory.decodeResource(getResources(), OTH_NAME[i]);
            if (i == 4) {
                OTHER[i] = scale(OTHER[i], 400, 35);
            } else if (i == 5) {
                OTHER[i] = scale(OTHER[i], 22, 22);
            } else {
                OTHER[i] = scale(OTHER[i], 80, 80);
            }
        }

      }

    public void ClearCanvas(Canvas cvs) {
        cvs.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    public void DrawLine(Canvas cvs, int a, int r, int g,int b, float lineWidth, float fromX, float fromY, float toX, float toY) {
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        mStrokePaint.setStrokeWidth(lineWidth);
        cvs.drawLine(fromX, fromY, toX, toY, mStrokePaint);
    }
	
    public void DrawRect(Canvas cvs, int a, int r, int g, int b, float stroke, float x, float y, float width, float height) {
        mStrokePaint.setStrokeWidth(stroke);
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        cvs.drawRoundRect(x, y, width,height, 5.0f, 5.0f, this.mStrokePaint);
    }
    
    public void DrawCurveRect(Canvas cvs, int a, int r, int g, int b, float stroke, float x, float y, float width, float height) {
        mStrokePaint.setStrokeWidth(stroke);
        mStrokePaint.setColor(Color.rgb(r, g, b));
        mStrokePaint.setAlpha(a);
        cvs.drawRoundRect(x, y, width,height, 5.0f, 5.0f, this.mStrokePaint);
    }

    public void DrawFilledRect2(Canvas cvs, int a, int r, int g, int b, float x, float y, float width, float height) {
        mFillPaint.setColor(Color.rgb(r, g, b));
        mFillPaint.setAlpha(a);
        cvs.drawRect(x, y, width, height, mFillPaint);
    }


    public void DrawFilledRect(Canvas cvs, int a, int r, int g, int b, float x, float y, float width, float height) {
        mFillPaint.setColor(Color.rgb(r, g, b));
        mFillPaint.setAlpha(a);
        cvs.drawRoundRect(x, y, width, height,5.0f,5.0f, mFillPaint);
    }
    
    public void DrawFilledRoundRect(Canvas cvs, int a, int r, int g, int b, float x, float y, float width, float height) {
        mFillPaint.setColor(Color.rgb(r, g, b));
        mFillPaint.setAlpha(a);
        cvs.drawRoundRect(x, y, width, height, 10.0f, 10.0f, mFillPaint);
    }
 
    public void DrawTextName(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mFPSText.setARGB(a, r, g, b);
        mFPSText.setTextSize(size);
        if (SystemClock.uptimeMillis() - mFPSTime > 1000) {
            mFPSTime = SystemClock.uptimeMillis();
            mFPS = mFPSCounter;
            mFPSCounter = 0f;
        } else {
            mFPSCounter++;
        }

        String fpsText = txt + mFPS;
        cvs.drawText(fpsText, posX, posY, mFPSText);
    }
    

    
    
    public void DrawTextMode(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mMDText.setARGB(a,r, g, b);
        mMDText.setTextSize(size);
        cvs.drawText("" + modeselect, posX, posY, mMDText);
    }

    public void DrawTextMode2(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mFPSText.setARGB(a,r, g, b);
        mFPSText.setTextSize(size);
        cvs.drawText("" + typelogin, posX, posY, mFPSText);
    }
    
    public void DrawTexture(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mMDText.setColor(Color.rgb(r, g, b));
        mMDText.setAlpha(a);
        mMDText.setTextSize(size);
        cvs.drawText(txt, posX, posY, mMDText);
    }

    public void DrawCustom(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mTexturePaint.setColor(Color.rgb(r, g, b));
        mTexturePaint.setAlpha(a);
        mTexturePaint.setTextSize(size);
        cvs.drawText("" + modeselect, posX, posY, mTexturePaint);
    }
    
    

    public void DrawFilledCurve(Canvas cvs, int a, int r, int g, int b, int x, int y, int width, int height) {
        int colors[] = {Color.TRANSPARENT, Color.rgb(r, g, b), Color.TRANSPARENT};
        GradientDrawable mDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setGradientRadius(2.0f * 60);
        Rect mRect = new Rect(x, y, width, height);
        mDrawable.setBounds(mRect);
        cvs.save();
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.draw(cvs);
        cvs.restore();
    }

    public void DrawFillRect(Canvas cvs, int a, int r, int g, int b, int x, int y, int width, int height) {
        int colors[] = {Color.argb(a,r,g,b), Color.argb(a,r,g,b), Color.argb(a,r,g,b)};
        GradientDrawable mDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setCornerRadius(6);
        mDrawable.setGradientRadius(2.0f * 60);
        Rect mRect = new Rect(x,y,width,height);
        mDrawable.setBounds(mRect);
        cvs.save();
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.draw(cvs);
        cvs.restore();
    }
    
    public void DrawTeamID(Canvas cvs, int a, int r, int g, int b,int teamid, float posX, float posY, float size) {
        mNamePaint.setColor(Color.rgb(r,g,b));
        mNamePaint.setTextSize(size);
        cvs.drawText(teamid + "", posX, posY, mNamePaint);
    }
    
    public void DrawTextBold(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        PaintTextBold.setARGB(a, r, g, b);
        PaintTextBold.setTextSize(size);
        cvs.drawText(txt, posX, posY, PaintTextBold);
    }

    public void DrawPlayerName(Canvas cvs, int a, int r, int g, int b, String nametxt, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String realname = new String(nameint);
        if(realname.length() > 5){
            realname = realname.substring(0, 5);
        }
        mNamePaint.setARGB(a,r, g, b);
        mNamePaint.setTextSize(size);
        cvs.drawText(realname, posX, posY, mNamePaint);
    }


    public void DrawOTH(Canvas cvs, int image_number, float X, float Y) {
        cvs.drawBitmap(OTHER[image_number], X, Y, p);
    }
    
	/*public void DrawItems(Canvas cvs, String itemName, float distance, float posX, float posY, float size) {
        String realItemName = getItemName(itemName);
        if (realItemName != null && !realItemName.equals("")) {
            mItemsPaint.setTextSize(size);
            if (realItemName.equals("LootBox")) {
                if (distance < 150) {
                    cvs.drawBitmap(lootBitmap, posX - 25, posY - (72 * mScaleY), mPaintBitmap);
                    cvs.drawText(realItemName + " (" + (int) distance + ")", posX, posY - 8, mItemsPaint);
                }
            }
             if (realItemName.equals("AirDrop")) {
                cvs.drawBitmap(airdropBitmap, posX - 25, posY - (72 * mScaleY), mPaintBitmap);
                cvs.drawText(realItemName + " (" + (int) distance + ")", posX, posY - 8, mItemsPaint);
            } else {
                mItemsPaint.setShadowLayer(3, 0, 0, Color.TRANSPARENT);
                cvs.drawText(realItemName + " (" + (int) distance + ")", posX, posY - 8, mItemsPaint);
            }
        }
    }*/

   public void DrawItems(Canvas cvs, String itemName, float distance, float posX, float posY, float size) {
        String realItemName = getItemName(itemName);
        if (realItemName != null && !realItemName.equals("")) {
            mItemsPaint.setTextSize(size);
            String displayText = realItemName + " (" + (int) distance + ")";
            mItemsPaint.setStyle(Paint.Style.STROKE);
            mItemsPaint.setStrokeWidth(3); // Set stroke width as needed
            mItemsPaint.setColor(Color.BLACK); // Set stroke color

            if (realItemName.equals("LootBox")) {
                if (distance < 150) {
                    cvs.drawBitmap(lootBitmap, posX - 25, posY - (72 * mScaleY), mPaintBitmap);
                    cvs.drawText(displayText, posX, posY - 8, mItemsPaint);
                    mItemsPaint.setStyle(Paint.Style.FILL);
                    mItemsPaint.setColor(Color.parseColor("#FF40CC7E")); // Set fill color
                    cvs.drawText(displayText, posX, posY - 8, mItemsPaint);
                }
            } else if (realItemName.equals("AirDrop")) {
                cvs.drawBitmap(airdropBitmap, posX - 25, posY - (72 * mScaleY), mPaintBitmap);
                cvs.drawText(displayText, posX, posY - 8, mItemsPaint);
                mItemsPaint.setStyle(Paint.Style.FILL);
                mItemsPaint.setColor(Color.parseColor("#FF0000"));
                cvs.drawText(displayText, posX, posY - 8, mItemsPaint);
            } else {
                mTextPainti.setTextSize(size);
                cvs.drawText(realItemName+" ("+Math.round(distance)+"m)", posX, posY, mTextPainti);
            }
        }
    }
    
    public void DrawDeadBoxItems(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mLootBoxPaint.setTextSize(size);
        mLootBoxPaint.setColor(Color.parseColor("#FF40CC7E"));
        cvs.drawText(txt, posX - 60, posY -10 , mLootBoxPaint);
    }
    
  /*  public void DrawItems(Canvas cvs, String itemName,float distance, float posX, float posY, float size) {
        String realItemName = getItemName(itemName);
        if(realItemName!=null && !realItemName.equals(""))
            mTextPaint.setTextSize(size);
            cvs.drawText(realItemName+" ("+Math.round(distance)+"m)", posX, posY, mTextPaint);
    }*/


    public void DrawVehicles(Canvas cvs, String VehicleName, float distance,float health, float fuel, float posX, float posY, float size) {
        String realVehicleName = VehicleName(VehicleName);
        if (realVehicleName != null && !realVehicleName.equals("")) {
            mVehiclesPaint.setTextSize(size);
            String displayText = realVehicleName + " (" + (int) distance + ")";
            mVehiclesPaint.setStyle(Paint.Style.STROKE);
            mVehiclesPaint.setStrokeWidth(3); // Set stroke width as needed
            mVehiclesPaint.setColor(Color.BLACK); // Set stroke color
            if (realVehicleName.equals("Buggy")) {
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                cvs.drawBitmap(buggyBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
            }        
            else if (realVehicleName.equals("UAZ")) {
                cvs.drawBitmap(uazBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Trike")) {
                cvs.drawBitmap(trikeBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Bike")) {
                cvs.drawBitmap(bikeBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Dacia")) {
                cvs.drawBitmap(daciaBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Jet")) {
                cvs.drawBitmap(jetBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Boat")) {
                cvs.drawBitmap(boatBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Bus")) {
                cvs.drawBitmap(busBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Mirado")) {
                cvs.drawBitmap(miradoBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Scooter")) {
                cvs.drawBitmap(scooterBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Rony")) {
                cvs.drawBitmap(ronyBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Snowbike")) {
                cvs.drawBitmap(snowbikeBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Snowmobile")) {
                cvs.drawBitmap(snowmobileBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Tempo")) {
                cvs.drawBitmap(tempoBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Truck")) {
                cvs.drawBitmap(truckBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("BRDM")) {
                cvs.drawBitmap(brdmBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Monster")) {
                cvs.drawBitmap(monsterBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("CoupeRB")) {
                cvs.drawBitmap(coupeBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("Motor Glider")) {
                cvs.drawBitmap(gliderBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("UTV")) {
                cvs.drawBitmap(utvBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            } else if (realVehicleName.equals("ATV1")) {
                cvs.drawBitmap(atvBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            }                                  
            else if (realVehicleName.equals("Reindeer")) {
                cvs.drawBitmap(kudaBitmap, posX - 25, posY - (60 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
              
            } else {
                cvs.drawBitmap(vehicleBitmap, posX - 25, posY - (50 * mScaleY), mPaintBitmap1);
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
                mVehiclesPaint.setStyle(Paint.Style.FILL);
                mVehiclesPaint.setColor(Color.parseColor("#FFFFFF"));
                cvs.drawText(displayText, posX, posY - 0, mVehiclesPaint);
            }         
            handleFuelHealthText(cvs, posX , posY - 10, fuel, health, size);
        }
    }
    
    public void DrawEnemyCount(Canvas cvs, int a, int r, int g, int b, int x, int y, int width, int height) {
        int colors[] = {Color.TRANSPARENT, Color.rgb(r,g,b), Color.TRANSPARENT};
        GradientDrawable mDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, colors);
        mDrawable.setShape(GradientDrawable.RECTANGLE);
        mDrawable.setGradientRadius(2.0f * 60);
        Rect mRect = new Rect(x,y,width,height);
        mDrawable.setBounds(mRect);
        cvs.save();
        mDrawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        mDrawable.draw(cvs);
        cvs.restore();
    }

    private void handleFuelHealthText(Canvas cvs, float posX, float posY, float fuel, float health, float size) {
        mStrokePaint.setARGB(50, 0, 0, 0); //Stroke
        cvs.drawRoundRect(posX - 45, posY + 19, posX + 50, posY + 25, 3, 3, mStrokePaint);
        mFilledPaint.setARGB(100, 255, 255, 0); //Health
        cvs.drawRoundRect(posX - 45, posY + 19, posX - 40 + (2 * 45) * fuel / 100, posY + 25, 3, 3, mFilledPaint);
        mStrokePaint.setARGB(50, 0, 0, 0); //Stroke
        cvs.drawRoundRect(posX - 45, posY + 29, posX + 50 , posY + 35, 3, 3, mStrokePaint);
        mFilledPaint.setARGB(100, 255, 45, 30); //Health
        cvs.drawRoundRect(posX - 45, posY + 29, posX - 40 + (2 * 45) * health / 100, posY + 35, 3, 3, mFilledPaint);
    }

    


    public void DrawCircle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float radius, float stroke) {
        mStrokePaint.setARGB(a, r, g, b);
        mStrokePaint.setStrokeWidth(stroke);
        cvs.drawCircle(posX, posY, radius, mStrokePaint);
    }

    public void DrawFilledTriangle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float size) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);

        float halfSize = size / 2;
        float height = (float) (Math.sqrt(3) * halfSize);

        float x1 = posX;
        float y1 = posY - height / 2;

        float x2 = posX - halfSize;
        float y2 = posY + height / 2;

        float x3 = posX + halfSize;
        float y3 = posY + height / 2;

        Path path = new Path();
        path.moveTo(x1, y1);
        path.lineTo(x2, y2);
        path.lineTo(x3, y3);
        path.close();

        cvs.drawPath(path, mFilledPaint);
    }

    public void DrawFilledCircle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float radius) {
        mFilledPaint.setColor(Color.rgb(r, g, b));
        mFilledPaint.setAlpha(a);
        cvs.drawCircle(posX, posY, radius, mFilledPaint);
    }
    
    public void DrawFillCircle(Canvas cvs, int a, int r, int g, int b, float posX, float posY, float radius, float stroke) {
        mFilledPaint.setARGB(a, r, g, b);
        mFilledPaint.setStrokeWidth(stroke);
        cvs.drawCircle(posX, posY, radius, mFilledPaint);
    }

    public void DrawWeapon(Canvas cvs, int a, int r, int g, int b, int id, int ammo, int ammo2, float posX, float posY, float size) {
        mNamePaint.setARGB(a, r, g, b);
        mNamePaint.setTextSize(size);
        //mTextPaint1.setShadowLayer(10, 1, 1, Color.rgb(1,1,1));
        String wname = getWeapon(id);
        if (wname != null) {
            if (wname == "Sickle" || wname == "Machete" || wname == "Crowbar" || wname == "Pan") {
                cvs.drawText(wname, posX, posY, mNamePaint);
            } else {
                cvs.drawText(wname + "(" + ammo + "/" + ammo2 + ")", posX, posY, mNamePaint);
            }
        }
    }

    public void DrawWeaponIcon(Canvas cvs, int id, float posX, float posY) {
        Bitmap cachedBitmap = bitmapCache.get(id);
        if (cachedBitmap != null) {
            cvs.drawBitmap(cachedBitmap, posX, posY - 43, null);
        } else {
            int weapon_icon = getWeaponIcon(id);
            if (weapon_icon != 0) {
                mScaleX = getWidth() / (float) 2340;
                mScaleY = getHeight() / (float) 1080;
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), weapon_icon);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, (int) (75 * mScaleX), (int) (40 * mScaleY), false);
                bitmapCache.put(id, scaledBitmap);
                cvs.drawBitmap(scaledBitmap, posX, posY - 43, null);
            }
        }
    }


    public void DrawOTH2(Canvas cvs, int image_number, float X, float Y) {
        cvs.drawBitmap(OTHER[image_number], X, Y, p);
    }

    public void DrawTextBot(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mNamePaint.setColor(Color.rgb(r, g, b));
        mNamePaint.setAlpha(a);
        cvs.drawText(txt, posX, posY, mNamePaint);
    }

    public void DrawPlayerID(Canvas cvs, int a, int r, int g, int b, String nametxt,int teamid, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        mNamePaint.setColor(Color.rgb(r,g,b));
        mNamePaint.setTextSize(size);
        mNamePaint.setShadowLayer(8.0f,1.5f,1.5f,Color.BLACK);
        cvs.drawText(teamid+"", posX, posY, mNamePaint);
    }

    public void DrawName1(Canvas cvs, int a, int r, int g, int b, String nametxt,int teamid, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String realname = new String(nameint);
        mNamePaint.setARGB(a,r, g, b);
        mNamePaint.setTextSize(size);
        mNamePaint.setShadowLayer(8.0f,1.5f,1.5f,Color.BLACK);
        
        cvs.drawText("   "+realname, posX, posY, mNamePaint);
    }

    public void DrawTriangle(Canvas cvs, int a, int r, int g, int b, float posX1, float posY1, float posX2, float posY2, float posX3, float posY3, float stroke) {
        Path path = new Path();
        path.moveTo(posX1, posY1); // Pindahkan ke titik awal segitiga
        path.lineTo(posX2, posY2); // Gambar garis ke titik kedua
        path.lineTo(posX3, posY3); // Gambar garis ke titik ketiga
        path.close(); // Tutup segitiga dengan menghubungkan titik terakhir dengan titik awal

        Paint paint = new Paint();
        paint.setARGB(a, r, g, b); // Set warna segitiga
        paint.setStyle(Paint.Style.STROKE); // Tetapkan gaya lukisan ke stroke (garis)
        paint.setStrokeWidth(stroke); // Tetapkan ketebalan garis

        cvs.drawPath(path, paint); // Gambar segitiga pada objek Canvas
    }

    public void DrawTriangleFilled(Canvas cvs, int a, int r, int g, int b, float posX1, float posY1, float posX2, float posY2, float posX3, float posY3) {
        Path path = new Path();
        path.moveTo(posX1, posY1); // Pindahkan ke titik awal segitiga
        path.lineTo(posX2, posY2); // Gambar garis ke titik kedua
        path.lineTo(posX3, posY3); // Gambar garis ke titik ketiga
        path.close(); // Tutup segitiga dengan menghubungkan titik terakhir dengan titik awal

        Paint paint = new Paint();
        paint.setARGB(a, r, g, b); // Set warna segitiga
        paint.setStyle(Paint.Style.FILL); // Tetapkan gaya lukisan ke pengisian (filled)

        cvs.drawPath(path, paint); // Gambar segitiga pada objek Canvas
    }

    public void DrawName2(Canvas cvs, int a, int r, int g, int b, String nametxt,int teamid, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String str = "";
        String realname = new String(nameint);
        mTextPaint.setARGB(a,r, g, b);
        mTextPaint.setTextSize(size);
        cvs.drawText(str+""+ Nation(realname), posX-80.0f, posY -30, mTextPaint);
    }
    
    public void DrawText(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mTextPaint.setARGB(a, r, g, b);
        mTextPaint.setTextSize(size);
        cvs.drawText(txt, posX, posY, mTextPaint);
    }
    
    public void DrawTexti(Canvas cvs, int a, int r, int g, int b, String txt, float posX, float posY, float size) {
        mTextPainti.setARGB(a, r, g, b);
        mTextPainti.setTextSize(size);
        cvs.drawText(txt, posX, posY, mTextPainti);
    }


    public void DrawName(Canvas cvs, int a, int r, int g, int b, String nametxt, int teamid, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String realname = new String(nameint);
        mTextPaint.setARGB(a, r, g, b);
        mTextPaint.setTextSize(size);
        mTextPaint.setShadowLayer(3, 2, 2, Color.BLACK);
        cvs.drawText("(" + teamid + ")" + realname, posX, posY, mTextPaint);
    }
    
    public void DrawUserID(Canvas cvs, int a, int r, int g, int b, String nametxt, float posX, float posY, float size) {
        String[] namesp = nametxt.split(":");
        char[] nameint = new char[namesp.length];
        for (int i = 0; i < namesp.length; i++)
            nameint[i] = (char) Integer.parseInt(namesp[i]);
        String realname = new String(nameint);
        mTextPaint.setARGB(a, r, g, b);
        mTextPaint.setTextSize(size);
        mTextPaint.setColor(Color.parseColor("#FFFFFF"));
        cvs.drawText("ID: " + realname, posX, posY, mTextPaint);
    }
    
    public void DrawNation(Canvas cvs, int a, int r, int g, int b, String nametxt, int flag, float posX, float posY, float size) {
            String[] namesp = nametxt.split(":");
            char[] nameint = new char[namesp.length];
            for (int i = 0; i < namesp.length; i++)
                nameint[i] = (char) Integer.parseInt(namesp[i]);
            String realname = new String(nameint);
            mTextPaint.setARGB(a, r, g, b);
            mTextPaint.setTextSize(size);
            cvs.drawText("", posX+85, posY-0, mTextPaint);
            cvs.drawText(Nation(realname), posX+73, posY-49, mTextPaint);
    }



    private String Nation(String code) {
            if (code.equals("G1"))
            {
                code = "🌍️";
            }
            else {
                code = new String(Character.toChars((Character.codePointAt(code, 0) - 65) + 127462)) + new String(Character.toChars((Character.codePointAt(code, 1) - 65) + 127462));
            }
            return code;
    }


    private String getWeapon(int id) {
        switch (id) {
                // AR
            case 101001:
            case 1010011:
            case 1010012:
            case 1010013:
            case 1010014:
            case 1010015:
                return "AKM";
            case 101002:
            case 1010021:
            case 1010022:
            case 1010023:
            case 1010024:
            case 1010025:
                return "M16A4";
            case 101003:
            case 1010031:
            case 1010032:
            case 1010033:
            case 1010034:
            case 1010035:
                return "SCAR-L";
            case 101004:
            case 1010041:
            case 1010042:
            case 1010043:
            case 1010044:
            case 1010045:
                return "M416";
            case 101005:
            case 1010051:
            case 1010052:
            case 1010053:
            case 1010054:
            case 1010055:
                return "Groza";
            case 101006:
            case 1010061:
            case 1010062:
            case 1010063:
            case 1010064:
            case 1010065:
                return "AUG";
            case 101007:
            case 1010071:
            case 1010072:
            case 1010073:
            case 1010074:
            case 1010075:
                return "QBZ";
            case 101008:
            case 1010081:
            case 1010082:
            case 1010083:
            case 1010084:
            case 1010085:
                return "M762";
            case 101009:
            case 1010091:
            case 1010092:
            case 1010093:
            case 1010094:
            case 1010095:
                return "Mk47";
            case 101010:
            case 1010101:
            case 1010102:
            case 1010103:
            case 1010104:
            case 1010105:
                return "G36C";
            case 101012:
            case 1010121:
            case 1010122:
            case 1010123:
            case 1010124:
            case 1010125:
                return "Honey Badger";
            case 101100:
            case 1011001:
            case 1011002:
            case 1011003:
            case 1011004:
            case 1011005:
                return "FAMAS";
            case 101101:
            case 1011011:
            case 1011012:
            case 1011013:
            case 1011014:
            case 1011015:
                return "ASM AR";
            case 101102:
            case 1011021:
            case 1011022:
            case 1011023:
            case 1011024:
            case 1011025:
                return "ACE32";

                // SMG
            case 102001:
            case 1020011:
            case 1020012:
            case 1020013:
            case 1020014:
            case 1020015:
                return "UZI";
            case 102002:
            case 1020021:
            case 1020022:
            case 1020023:
            case 1020024:
            case 1020025:
                return "UMP";
            case 102003:
            case 1020031:
            case 1020032:
            case 1020033:
            case 1020034:
            case 1020035:
                return "Vector";
            case 102004:
            case 1020041:
            case 1020042:
            case 1020043:
            case 1020044:
            case 1020045:
                return "ThommyGun";
            case 102005:
            case 1020051:
            case 1020052:
            case 1020053:
            case 1020054:
            case 1020055:
                return "Bizon";
            case 102007:
            case 1020071:
            case 1020072:
            case 1020073:
            case 1020074:
            case 1020075:
                return "MP5K";
            case 102105:
            case 1021051:
            case 1021052:
            case 1021053:
            case 1021054:
            case 1021055:
                return "P90";
                // Snipers
            case 103001:
            case 1030011:
            case 1030012:
            case 1030013:
            case 1030014:
            case 1030015:
                return "Kar98k";
            case 103002:
            case 1030021:
            case 1030022:
            case 1030023:
            case 1030024:
            case 1030025:
                return "M24";
            case 103003:
            case 1030031:
            case 1030032:
            case 1030033:
            case 1030034:
            case 1030035:
                return "AWM";
            case 103004:
            case 1030041:
            case 1030042:
            case 1030043:
            case 1030044:
            case 1030045:
                return "SKS";
            case 103005:
            case 1030051:
            case 1030052:
            case 1030053:
            case 1030054:
            case 1030055:
                return "VSS";
            case 103006:
            case 1030061:
            case 1030062:
            case 1030063:
            case 1030064:
            case 1030065:
                return "Mini14";
            case 103007:
            case 1030071:
            case 1030072:
            case 1030073:
            case 1030074:
            case 1030075:
                return "Mk14";
            case 103008:
            case 1030081:
            case 1030082:
            case 1030083:
            case 1030084:
            case 1030085:
                return "Win94";
            case 103009:
            case 1030091:
            case 1030092:
            case 1030093:
            case 1030094:
            case 1030095:
                return "SLR";
            case 103010:
            case 1030101:
            case 1030102:
            case 1030103:
            case 1030104:
            case 1030105:
                return "QBU";
            case 103011:
            case 1030111:
            case 1030112:
            case 1030113:
            case 1030114:
            case 1030115:
                return "Mosin";
            case 103012:
            case 1030121:
            case 1030122:
            case 1030123:
            case 1030124:
            case 1030125:
                return "Lynx AMR";
            case 103100:
            case 1031001:
            case 1031002:
            case 1031003:
            case 1031004:
            case 1031005:
                return "Mk12";

                // Shotguns and hand weapons
            case 104001:
            case 1040011:
            case 1040012:
            case 1040013:
            case 1040014:
            case 1040015:
                return "S686";
            case 104002:
            case 1040021:
            case 1040022:
            case 1040023:
            case 1040024:
            case 1040025:
                return "S1897";
            case 104003:
            case 1040031:
            case 1040032:
            case 1040033:
            case 1040034:
            case 1040035:
                return "S12K";
            case 104004:
            case 1040041:
            case 1040042:
            case 1040043:
            case 1040044:
            case 1040045:
                return "DBS";
            case 104101:
            case 1041011:
            case 1041012:
            case 1041013:
            case 1041014:
            case 1041015:
                return "M1014";
            case 104102:
            case 1041021:
            case 1041022:
            case 1041023:
            case 1041024:
            case 1041025:
                return "NS2000";

                // Melee Weapons
            case 108001:
            case 1080011:
            case 1080012:
            case 1080013:
            case 1080014:
            case 1080015:
                return "Machete";
            case 108002:
            case 1080021:
            case 1080022:
            case 1080023:
            case 1080024:
            case 1080025:
                return "Crowbar";
            case 108003:
            case 1080031:
            case 1080032:
            case 1080033:
            case 1080034:
            case 1080035:
                return "Sickle";
            case 108004:
            case 1080041:
            case 1080042:
            case 1080043:
            case 1080044:
            case 1080045:
                return "Panci";
            case 108005:
            case 1080051:
            case 1080052:
            case 1080053:
            case 1080054:
            case 1080055:
                return "Knife";

                // Crossbow
            case 107001:
            case 1070011:
            case 1070012:
            case 1070013:
            case 1070014:
            case 1070015:
                return "Crossbow";

                // Other
            case 105002:
            case 1050021:
            case 1050022:
            case 1050023:
            case 1050024:
            case 1050025:
                return "DP28";
            case 105001:
            case 1050011:
            case 1050012:
            case 1050013:
            case 1050014:
            case 1050015:
                return "M249";
            case 105010:
            case 1050101:
            case 1050102:
            case 1050103:
            case 1050104:
            case 1050105:
                return "MG3";

                // Pistols
            case 106006:
            case 1060061:
            case 1060062:
            case 1060063:
            case 1060064:
            case 1060065:
                return "Sawed Off";
            case 106003:
            case 1060031:
            case 1060032:
            case 1060033:
            case 1060034:
            case 1060035:
                return "R1895";
            case 106008:
            case 1060081:
            case 1060082:
            case 1060083:
            case 1060084:
            case 1060085:
                return "Scorpion";
            case 106001:
            case 1060011:
            case 1060012:
            case 1060013:
            case 1060014:
            case 1060015:
                return "P92";
            case 106004:
            case 1060041:
            case 1060042:
            case 1060043:
            case 1060044:
            case 1060045:
                return "P18C";
            case 106005:
            case 1060051:
            case 1060052:
            case 1060053:
            case 1060054:
            case 1060055:
                return "R45";
            case 106002:
            case 1060021:
            case 1060022:
            case 1060023:
            case 1060024:
            case 1060025:
                return "P1911";
            case 106010:
            case 1060101:
            case 1060102:
            case 1060103:
            case 1060104:
            case 1060105:
                return "Desert Angle";
        }
        return "";
    }

    private int getWeaponIcon(int id) {
        //AR and SMG
        if (id == 101006)
            return R.drawable.c101006;
        if (id == 101008)
            return R.drawable.c101008;
        if (id == 101003)
            return R.drawable.c101003;
        if (id == 101004)
            return R.drawable.c101004;
        if (id == 101002)
            return R.drawable.c101002;
        if (id == 101009)
            return R.drawable.c101009;
        if (id == 101010)
            return R.drawable.c101010;
        if (id == 101007)
            return R.drawable.c101007;
        if (id == 101001)
            return R.drawable.c101001;
        if (id == 101005)
            return R.drawable.c101005;
        if (id == 102005)
            return R.drawable.c102005;
        if (id == 102004)
            return R.drawable.c102004;
        if (id == 102007)
            return R.drawable.c102007;
        if (id == 102002)
            return R.drawable.c102002;
        if (id == 102003)
            return R.drawable.c102003;
        if (id == 102001)
            return R.drawable.c102001;
        if (id == 105002)
            return R.drawable.c105002;
        if (id == 105001)
            return R.drawable.c105001;

        //Snipers
        if (id == 103003)
            return R.drawable.c103003;
        if (id == 103010)
            return R.drawable.c103010;
        if (id == 103009)
            return R.drawable.c103009;
        if (id == 103004)
            return R.drawable.c103004;
        if (id == 103006)
            return R.drawable.c103006;
        if (id == 103002)
            return R.drawable.c103002;
        if (id == 103001)
            return R.drawable.c103001;
        if (id == 103005)
            return R.drawable.c103005;
        if (id == 103008)
            return R.drawable.c103008;
        if (id == 103007)
            return R.drawable.c103007;

        //Shotguns and Hand weapons
        if (id == 104003)
            return R.drawable.c104003;
        if (id == 104004)
            return R.drawable.c104004;
        if (id == 104001)
            return R.drawable.c104001;
        if (id == 104002)
            return R.drawable.c104002;
        if (id == 108003)
            return R.drawable.c108003;
        if (id == 108001)
            return R.drawable.c108001;
        if (id == 108002)
            return R.drawable.c108002;
        if (id == 107001)
            return R.drawable.c107001;
        if (id == 108004)
            return R.drawable.c108004;

        //Pistols
        if (id == 106006)
            return R.drawable.c106006;
        if (id == 106003)
            return R.drawable.c106003;
        if (id == 106008)
            return R.drawable.c106008;
        if (id == 106001)
            return R.drawable.c106001;
        if (id == 106004)
            return R.drawable.c106004;
        if (id == 106005)
            return R.drawable.c106005;
        if (id == 106002)
            return R.drawable.c106002;
        if (id == 106010)
            return R.drawable.c106010;

        return 0;
    }

    private String getItemName(String s) {
        //Scopes
        if (s.contains("MZJ_8X") && getConfig("8x")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "8x";
        }
        if (s.contains("MZJ_2X") && getConfig("2x")) {
            mTextPainti.setARGB(255, 230, 172, 226);
            return "2x";
        }
        if (s.contains("MZJ_HD") && getConfig("Red Dot")) {
            mTextPainti.setARGB(255, 230, 172, 226);
            return "Red Dot";
        }
        if (s.contains("MZJ_3X") && getConfig("3x")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "3X";
        }
        if (s.contains("MZJ_QX") && getConfig("Hollow")) {
            mTextPainti.setARGB(255, 153, 75, 152);
            return "Hollow Sight";
        }
        if (s.contains("MZJ_6X") && getConfig("6x")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "6x";
        }
        if (s.contains("MZJ_4X") && getConfig("4x")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "4x";
        }
        if (s.contains("MZJ_SideRMR") && getConfig("Canted")) {
            mTextPainti.setARGB(255, 153, 75, 152);
            return "Canted Sight";
        }

        //Assault Rifle
        if (s.contains("Rifle_AUG") && getConfig("AUG")) {
            mTextPainti.setARGB(255, 52, 224, 63);
            return "AUG";
        }
        if (s.contains("Rifle_M762") && getConfig("M762")) {
            mTextPainti.setARGB(255, 43, 26, 28);
            return "M762";
        }
        if (s.contains("Rifle_SCAR") && getConfig("SCAR-L")) {
            mTextPainti.setARGB(255, 52, 224, 63);
            return "SCAR-L";
        }
        if (s.contains("Rifle_FAMAS") && getConfig("FAMAS")) {
            mTextPainti.setARGB(255, 0, 255, 0);
            return "FAMAS";
        }
        if (s.contains("Rifle_M416") && getConfig("M416")) {
            mTextPainti.setARGB(255, 115, 235, 223);
            return "M416";
        }
        if (s.contains("Rifle_M16A4") && getConfig("M16A4")) {
            mTextPainti.setARGB(255, 116, 227, 123);
            return "M16A-4";
        }
        if (s.contains("Rifle_G36") && getConfig("G36C")) {
            mTextPainti.setARGB(255, 116, 227, 123);
            return "G36C";
        }
        if (s.contains("Rifle_QBZ") && getConfig("QBZ")) {
            mTextPainti.setARGB(255, 52, 224, 63);
            return "QBZ";
        }
        if (s.contains("Rifle_AKM") && getConfig("AKM")) {
            mTextPainti.setARGB(255, 214, 99, 99);
            return "AKM";
        }
        if (s.contains("Rifle_HoneyBadger") && getConfig("Honey Badger")) {
            mTextPainti.setARGB(255, 214, 99, 99);
            return "Honey Badger";
        }
        if (s.contains("Rifle_Groza") && getConfig("Groza")) {
            mTextPainti.setARGB(255, 214, 99, 99);
            return "Groza";
        }
        if (s.contains("Rifle_ACE32") && getConfig("ACE32")) {
            mTextPainti.setARGB(255, 214, 99, 99);
            return "ACE32";
        }

        //Contra

        if (s.contains("SubmachineGun_UMP45") && getConfig("UMP")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "UMP";
        }
        //Sub Machine Gun
        if (s.contains("MachineGun_PP19") && getConfig("Bizon")) {
            mTextPainti.setARGB(255, 255, 246, 0);
            return "Bizon";
        }
        if (s.contains("MachineGun_TommyGun") && getConfig("TommyGun")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "TommyGun";
        }
        if (s.contains("MachineGun_MP5K") && getConfig("MP5K")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "MP5K";
        }
        if (s.contains("MachineGun_UMP9") && getConfig("UMP")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "UMP";
        }
        if (s.contains("MachineGun_Vector") && getConfig("Vector")) {
            mTextPainti.setARGB(255, 255, 246, 0);
            return "Vector";
        }
        if (s.contains("MachineGun_Uzi") && getConfig("UZI")) {
            mTextPainti.setARGB(255, 255, 246, 0);
            return "UZI";
        }
        if (s.contains("MachineGun_P90") && getConfig("P90")) {
            mTextPainti.setARGB(255, 233, 0, 207);
            return "P90";
        }

        //Other Gun
        if (s.contains("Other_DP28") && getConfig("DP28")) {
            mTextPainti.setARGB(255, 43, 26, 28);
            return "DP28";
        }
        if (s.contains("Other_M249") && getConfig("M249")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "M249";
        }
        if (s.contains("Other_MG3") && getConfig("MG3")) {
            mTextPainti.setARGB(255, 0, 255, 0);
            return "MG3";
        }

        //Snipers
        if (s.contains("Sniper_AWM") && getConfig("AWM")) {
            mTextPainti.setColor(Color.BLACK);
            return "AWM";
        }
        if (s.contains("Sniper_AMR") && getConfig("AMR")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "AMR";
        }
        if (s.contains("Sniper_QBU") && getConfig("QBU")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "QBU";
        }
        if (s.contains("Sniper_SLR") && getConfig("SLR")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "SLR";
        }
        if (s.contains("Sniper_SKS") && getConfig("SKS")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "SKS";
        }
        if (s.contains("Sniper_Mini14") && getConfig("Mini14")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "Mini14";
        }
        if (s.contains("Sniper_M24") && getConfig("M24")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "M24";
        }
        if (s.contains("Sniper_Kar98k") && getConfig("Kar98k")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "Kar98k";
        }
        if (s.contains("Sniper_VSS") && getConfig("VSS")) {
            mTextPainti.setARGB(255, 255, 246, 0);
            return "VSS";
        }
        if (s.contains("Sniper_Win94") && getConfig("Win94")) {
            mTextPainti.setARGB(255, 207, 207, 207);
            return "Win94";
        }
        if (s.contains("Sniper_Mk14") && getConfig("MK14")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "MK14";
        }
        if (s.contains("Sniper_Mosin") && getConfig("Mosin")) {
            mTextPainti.setARGB(255, 153, 0, 0);
            return "Mosin";
        }
        if (s.contains("Sniper_MK12") && getConfig("MK12")) {
            this.mTextPainti.setARGB(255, 214, 99, 99);
            return "MK12";
        }
        if (s.contains("Sniper_Mk47") && getConfig("MK47")) {
            mTextPainti.setARGB(255, 247, 99, 245);
            return "Mk47 Mutant";
        }

        //Shotguns
        if (s.contains("ShotGun_S12K") && getConfig("S12K")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "S12K";
        }
        if (s.contains("ShotGun_DP12") && getConfig("DBS")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "DBS";
        }
        if (s.contains("ShotGun_M1014") && getConfig("M1014")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "M1014";
        }
        if (s.contains("ShotGun_Neostead2000") && getConfig("NS2000")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "NS2000";
        }
        if (s.contains("ShotGun_S686") && getConfig("S686")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "S686";
        }
        if (s.contains("ShotGun_S1897") && getConfig("S1897")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "S1897";
        }

        //
        if (s.contains("Sickle") && getConfig("Sickle")) {
            mTextPainti.setARGB(255, 102, 74, 74);
            return "Sickle";
        }
        if (s.contains("Machete") && getConfig("Machete")) {
            mTextPainti.setARGB(255, 102, 74, 74);
            return "Machete";
        }
        if (s.contains("Cowbar") && getConfig("Crowbar")) {
            mTextPainti.setARGB(255, 102, 74, 74);
            return "Crowbar";
        }
        if (s.contains("CrossBow") && getConfig("CrossBow")) {
            mTextPainti.setARGB(255, 102, 74, 74);
            return "CrossBow";
        }
        if (s.contains("Pan") && getConfig("Pan")) {
            mTextPainti.setARGB(255, 102, 74, 74);
            return "Pan";
        }

        //Pistols
        if (s.contains("SawedOff") && getConfig("Sawed-Off")) {
            mTextPainti.setARGB(255, 153, 109, 109);
            return "SawedOff";
        }
        if (s.contains("R1895") && getConfig("R1895")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "R1895";
        }
        if (s.contains("Vz61") && getConfig("Scorpion")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "Scorpion";
        }
        if (s.contains("P92") && getConfig("P92")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "P92";
        }
        if (s.contains("P18C") && getConfig("P18C")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "P18C";
        }
        if (s.contains("R45") && getConfig("R45")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "R45";
        }
        if (s.contains("P1911") && getConfig("P1911")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "P1911";
        }
        if (s.contains("DesertEagle") && getConfig("Dessert Eagle")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "DesertEagle";
        }

        //Ammo
        if (s.contains("Ammo_762mm") && getConfig("7.62mm")) {
            mTextPainti.setARGB(255, 92, 36, 28);
            return "7.62";
        }
        if (s.contains("Ammo_45AC") && getConfig("45ACP")) {
            mTextPainti.setColor(Color.LTGRAY);
            return "45ACP";
        }
        if (s.contains("Ammo_556mm") && getConfig("5.56mm")) {
            mTextPainti.setColor(Color.GREEN);
            return "5.56";
        }
        if (s.contains("Ammo_9mm") && getConfig("9mm")) {
            mTextPainti.setColor(Color.YELLOW);
            return "9mm";
        }
        if (s.contains("Ammo_300Magnum") && getConfig("300Magnum")) {
            mTextPainti.setColor(Color.BLACK);
            return "300Magnum";
        }
        if (s.contains("Ammo_50BMG") && getConfig("50BMG")) {
            mTextPainti.setColor(Color.BLACK);
            return "50BMG";
        }
        if (s.contains("Ammo_12Guage") && getConfig("12Guage")) {
            mTextPainti.setARGB(255, 156, 91, 81);
            return "12Guage";
        }
        if (s.contains("Ammo_Bolt") && getConfig("Arrow")) {
            mTextPainti.setARGB(255, 156, 113, 81);
            return "Arrow";
        }

        //bag helmet vest
        if (s.contains("Bag_Lv3") && getConfig("Bag L3")) { mTextPainti.setARGB(255, 36, 83, 255);
            return "Bag lvl 3";
        }

        if (s.contains("Bag_Lv1")  && getConfig("Bag L1")) { mTextPainti.setARGB(255, 127, 154, 250);
            return "Bag lvl 1";
        }

        if (s.contains("Bag_Lv2") && getConfig("Bag L2")) { mTextPainti.setARGB(255, 77, 115, 255);
            return "Bag lvl 2";
        }

        if (s.contains("Armor_Lv2") && getConfig("Vest L2")) { mTextPainti.setARGB(255, 77, 115, 255);
            return "Vest lvl 2";
        }


        if (s.contains("Armor_Lv1") && getConfig("Vest L1")) { mTextPainti.setARGB(255, 127, 154, 250);
            return "Vest lvl 1";
        }


        if (s.contains("Armor_Lv3") && getConfig("Vest L3")) { mTextPainti.setARGB(255, 36, 83, 255);
            return "Vest lvl 3";
        }


        if (s.contains("Helmet_Lv2") && getConfig("Helmet L2")) { mTextPainti.setARGB(255, 77, 115, 255);
            return "Helmet lvl 2";
        }

        if (s.contains("Helmet_Lv1") && getConfig("Helmet L1")) { mTextPainti.setARGB(255, 127, 154, 250);
            return "Helmet lvl 1";
        }

        if (s.contains("Helmet_Lv3") && getConfig("Helmet L3")) { mTextPainti.setARGB(255, 36, 83, 255);
            return "Helmet lvl 3";
        }

        //Healthkits
        if (s.contains("Pills") && getConfig("PainKiller")) { mTextPainti.setARGB(255, 227, 91, 54);
            return "PainKiller";
        }

        if (s.contains("Injection") && getConfig("Injection")) { mTextPainti.setARGB(255,204, 193, 190);
            return "Injection";
        }

        if (s.contains("Drink") && getConfig("EnergyDrink")) { mTextPainti.setARGB(255, 54, 175, 227);
            return "Energy Drink";
        }

        if (s.contains("Firstaid") && getConfig("FirstAid")) { mTextPainti.setARGB(255, 194, 188, 109);
            return "FirstAid";
        }

        if (s.contains("Bandage") && getConfig("Bandage")) { mTextPainti.setARGB(255, 43, 189, 48);
            return "Bandage";
        }

        if (s.contains("FirstAidbox") && getConfig("MedKit")) { mTextPainti.setARGB(255, 0, 171, 6);
            return "Medkit";
        }

        //Throwables
        if (s.contains("Grenade_Stun") && getConfig("Stun")) { mTextPainti.setARGB(255,204, 193, 190);
            return "Stun";
        }

        if (s.contains("Grenade_Shoulei") && getConfig("Grenade")) { mTextPainti.setARGB(255, 2, 77, 4);
            return "Grenade";
        }

        if (s.contains("Grenade_Smoke") && getConfig("Smoke")) { mTextPainti.setColor(Color.WHITE);
            return "Smoke";
        }

        if (s.contains("Grenade_Burn") && getConfig("Molotov")) { mTextPainti.setARGB(255, 230, 175, 64);
            return "Molotov";
        }

        //others
        if (s.contains("Large_FlashHider") && getConfig("Flash Hider Ar")) { mTextPainti.setARGB(255, 255, 213, 130);
            return "Flash Hider Ar";
        }

        if (s.contains("QK_Large_C") && getConfig("Compensator Ar")) { mTextPainti.setARGB(255, 255, 213, 130);
            return "Compensator Ar";
        }

        if (s.contains("Mid_FlashHider") && getConfig("Flash Hider SMG")) { mTextPainti.setARGB(255, 255, 213, 130);
            return "Flash Hider SMG";
        }

        if (s.contains("QT_A_") && getConfig("Tactical Stock")) { mTextPainti.setARGB(255, 158, 222, 195);
            return "Tactical Stock";
        }

        if (s.contains("DuckBill") && getConfig("Duckbill")) { mTextPainti.setARGB(255, 158, 222, 195);
            return "DuckBill";
        }

        if (s.contains("Sniper_FlashHider") && getConfig("Flash Hider Sniper")) { mTextPainti.setARGB(255, 158, 222, 195);
            return "Flash Hider Sniper";
        }

        if (s.contains("Mid_Suppressor") && getConfig("Suppressor SMG")) { mTextPainti.setARGB(255, 158, 222, 195);
            return "Suppressor SMG";
        }

        if (s.contains("Choke") && getConfig("Choke")) { mTextPainti.setARGB(255, 155, 189, 222);
            return "Choke";
        }

        if (s.contains("QT_UZI") && getConfig("Stock Micro UZI")) { mTextPainti.setARGB(255, 155, 189, 222);
            return "Stock Micro UZI";
        }

        if (s.contains("QK_Sniper") && getConfig("Compensator Sniper")) { mTextPainti.setARGB(255, 60, 127, 194);
            return "Compensator Sniper";
        }

        if (s.contains("Sniper_Suppressor") && getConfig("Suppressor Sniper")) { mTextPainti.setARGB(255, 60, 127, 194);
            return "Suppressor Sniper";
        }

        if (s.contains("Large_Suppressor") && getConfig("Suppressor Ar")) { mTextPainti.setARGB(255, 60, 127, 194);
            return "Suppressor Ar";
        }


        if (s.contains("Sniper_EQ_") && getConfig("Extended QD Sniper")) { mTextPainti.setARGB(255, 193, 140, 222);
            return "Ex.Qd.Sniper";
        }

        if (s.contains("Sniper_E_") && getConfig("Extended Mag Sniper")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "Ex.Sniper";
        }

        if (s.contains("Sniper_Q_") && getConfig("QuickDraw Mag Sniper")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "Qd.Sniper";
        }

        if (s.contains("Large_EQ_") && getConfig("Extended QD Ar")) { mTextPainti.setARGB(255, 193, 140, 222);
            return "Extended QD Ar";
        }

        if (s.contains("Large_E_") && getConfig("Extended Mag Ar")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "Extended Mag Ar";
        }

        if (s.contains("Large_Q_") && getConfig("QuickDraw Mag Ar")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "QuickDraw Mag Ar";
        }

        if (s.contains("Mid_EQ_") && getConfig("Extended QD SMG")) { mTextPainti.setARGB(255, 193, 140, 222);
            return "Ex.Qd.SMG";
        }

        if (s.contains("Mid_E_") && getConfig("Extended Mag SMG")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "Ex.SMG";
        }

        if (s.contains("Mid_Q_") && getConfig("QuickDraw Mag SMG")) { mTextPainti.setARGB(255, 193, 163, 209);
            return "Qd.SMG";
        }

        if (s.contains("Crossbow_Q") && getConfig("Quiver CrossBow")) { mTextPainti.setARGB(255, 148, 121, 163);
            return "Quiver CrossBow";
        }

        if (s.contains("ZDD_Sniper") && getConfig("Bullet Loop")) { mTextPainti.setARGB(255, 148, 121, 163);
            return "Bullet Loop";
        }


        if (s.contains("ThumbGrip") && getConfig("Thumb Grip")) { mTextPainti.setARGB(255, 148, 121, 163);
            return "Thumb Grip";
        }

        if (s.contains("Lasersight") && getConfig("Laser Sight")) { mTextPainti.setARGB(255, 148, 121, 163);
            return "Laser Sight";
        }

        if (s.contains("Angled") && getConfig("Angled Grip")) { mTextPainti.setARGB(255, 219, 219, 219);
            return "Angled Grip";
        }

        if (s.contains("LightGrip") && getConfig("Light Grip")) { mTextPainti.setARGB(255, 219, 219, 219);
            return "Light Grip";
        }

        if (s.contains("Vertical") && getConfig("Vertical Grip")) { mTextPainti.setARGB(255, 219, 219, 219);
            return "Vertical Grip";
        }

        if (s.contains("HalfGrip") && getConfig("Half Grip")) { mTextPainti.setARGB(255, 155, 189, 222);
            return "Half Grip";
        }


        if (s.contains("GasCan") && getConfig("Gas Can")) { mTextPainti.setARGB(255, 255, 143, 203);
            return "Gas Can";
        }

        if (s.contains("Mid_Compensator") && getConfig("Compensator SMG")) { mTextPainti.setARGB(255, 219, 219, 219);
            return "Compensator SMG";
        }

        //special
        if (s.contains("Flaregun") && getConfig("FlareGun")) { mTextPainti.setARGB(255, 242, 63, 159);
            return "Flare Gun";
        }
        if (s.contains("Ammo_Flare") && getConfig("FlareGun")) { mTextPainti.setARGB(255, 242, 63, 159);
            return "Flare Gun";
        }

        if (s.contains("Ghillie") && getConfig("Ghillie Suit")) { mTextPainti.setARGB(255, 139, 247, 67);
            return "Ghillie Suit";
        }
        if (s.contains("CheekPad") && getConfig("CheekPad")) { mTextPainti.setARGB(255, 112, 55, 55);
            return "CheekPad";
        }
        if ( s.contains("PickUpListWrapperActor") && getConfig("LootBox")) { mTextPainti.setARGB(255, 255, 255, 255);
            return "LootBox";
        }
        if ((s.contains("AirDropPlane")) && getConfig("DropPlane")) { mTextPainti.setARGB(255, 0, 255, 255);
            return "DropPlane";
        }
        if ((s.contains("AirDropBox")) && getConfig("AirDrop")) { mTextPainti.setARGB(255, 0, 200, 0);
            return "AirDrop";
        }
        return null;

    }
    
    private String VehicleName(String s) {
        if (s.contains("Buggy") && getConfig("Buggy"))
            return "Buggy";
        if (s.contains("UAZ") && getConfig("UAZ"))
            return "UAZ";
        if (s.contains("MotorcycleC") && getConfig("Trike"))
            return "Trike";
        if (s.contains("Motorcycle") && getConfig("Bike"))
            return "Bike";
        if (s.contains("DAcia") && getConfig("Dacia"))
            return "Dacia";
        if (s.contains("Dacia") && getConfig("Dacia"))
            return "Dacia";    
        if (s.contains("AquaRail") && getConfig("Jet"))
            return "Jet";
        if (s.contains("PG117") && getConfig("Boat"))
            return "Boat";
        if (s.contains("MiniBus") && getConfig("Bus"))
            return "Bus";
        if (s.contains("Mirado") && getConfig("Mirado"))
            return "Mirado";
        if (s.contains("Scooter") && getConfig("Scooter"))
            return "Scooter";
        if (s.contains("Rony") && getConfig("Rony"))
            return "Rony";
        if (s.contains("Snowbike") && getConfig("Snowbike"))
            return "Snowbike";
        if (s.contains("Snowmobile") && getConfig("Snowmobile"))
            return "Snowmobile";
        if (s.contains("Tuk") && getConfig("Tempo"))
            return "Tempo";
        if (s.contains("PickUp") && getConfig("Truck"))
            return "Truck";
        if (s.contains("BRDM") && getConfig("BRDM"))
            return "BRDM";
        if (s.contains("LadaNiva") && getConfig("LadaNiva"))
            return "LadaNiva";
        if (s.contains("Bigfoot") && getConfig("Monster"))
            return "Monster";
        if (s.contains("CoupeRB") && getConfig("CoupeRB"))
            return "CoupeRB";
		if (s.contains("glider") && getConfig("Motor Glider"))
            return "Motor Glider";
        if(s.contains("UTV") && getConfig("UTV"))
            return "UTV";
        if(s.contains("ATV1") && getConfig("ATV1"))
            return "ATV1";
        if(s.contains("Reindeer") && getConfig("Reindeer"))
            return "Reindeer";    
        return "";
    }

    public static Bitmap scale(Bitmap bitmap, int maxWidth, int maxHeight) {
        // Determine the constrained dimension, which determines both dimensions.
        int width;
        int height;
        float widthRatio = (float)bitmap.getWidth() / maxWidth;
        float heightRatio = (float)bitmap.getHeight() / maxHeight;
        // Width constrained.
        if (widthRatio >= heightRatio) {
            width = maxWidth;
            height = (int)(((float)width / bitmap.getWidth()) * bitmap.getHeight());
        } else {
            height = maxHeight;
            width = (int)(((float)height / bitmap.getHeight()) * bitmap.getWidth());
        }
        Bitmap scaledBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        float ratioX = (float)width / bitmap.getWidth();
        float ratioY = (float)height / bitmap.getHeight();
        float middleX = width / 2.0f;
        float middleY = height / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bitmap, middleX - bitmap.getWidth() / 2, middleY - bitmap.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

}

