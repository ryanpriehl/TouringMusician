/* Copyright 2016 Google Inc.
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

package com.google.engedu.touringmusician;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

public class TourMap extends View {

    private Bitmap mapImage;
    private CircularLinkedList list = new CircularLinkedList();
    private CircularLinkedList list2 = new CircularLinkedList();
    private CircularLinkedList list3 = new CircularLinkedList();
    private String insertMode = "Beginning";

    public TourMap(Context context) {
        super(context);
        mapImage = BitmapFactory.decodeResource(
                getResources(),
                R.drawable.map);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mapImage, 0, 0, null);
        Paint pointPaint = new Paint();
        pointPaint.setColor(Color.RED);

        drawList(list, canvas, pointPaint);
        drawList(list2, canvas, pointPaint);
        drawList(list3, canvas, pointPaint);
    }

    protected void drawList(CircularLinkedList list, Canvas canvas, Paint pointPaint){
        Point first = null;
        Point prev = null;
        for (Point p : list) {
            pointPaint.setColor(Color.RED);
            canvas.drawCircle(p.x, p.y, 20, pointPaint);

            pointPaint.setColor(Color.BLACK);
            if(prev != null){
                canvas.drawLine(prev.x, prev.y, p.x, p.y, pointPaint);
                prev = p;
            }
            else{
                first = prev = p;
            }
        }
        if(first != null && prev != null){
            canvas.drawLine(prev.x, prev.y, first.x, first.y, pointPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Point p = new Point((int) event.getX(), (int)event.getY());
                if (insertMode.equals("Beginning")) {
                    list.insertBeginning(p);
                } else if (insertMode.equals("Closest")) {
                    list.insertNearest(p);
                } else if (insertMode.equals("Smallest")) {
                    list.insertSmallest(p);
                } else {
                    list.insertBeginning(p);
                    list2.insertNearest(p);
                    list3.insertSmallest(p);
                }
                TextView message = (TextView) ((Activity) getContext()).findViewById(R.id.game_status);
                if (message != null && !insertMode.equals("All")) {
                    message.setText(String.format("Tour length is now %.2f", list.totalDistance()));
                } else {
                    message.setText(String.format(  "\"Beginning\" tour length: " + list.totalDistance()
                                                    +"\n\"Closest\" tour length: " + list2.totalDistance()
                                                    +"\n\"Smallest\" tour length: " + list3.totalDistance()));
                }
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
    }

    public void reset() {
        list.reset();
        list2.reset();
        list3.reset();
        invalidate();
    }

    public void setInsertMode(String mode) {
        insertMode = mode;
    }
}
