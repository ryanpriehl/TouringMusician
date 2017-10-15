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


import android.graphics.Point;

import java.util.ArrayList;
import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

        public Node(Point p){
            point = p;
            prev = next = null;
        }

    }

    Node head;
    Node tail;

    public CircularLinkedList(){
        head = tail = null;
    }

    public void insertHead(Point p){
        Node node = new Node(p);
        head = node;
        head.next = head.prev = head;
    }

    public void insertTail(Point p){
        Node node = new Node(p);
        tail = head;
        head = node;
        head.next = head.prev = tail;
        tail.next = tail.prev = head;
    }

    public void insertAfter(Node newNode, Node n){
        newNode.prev = n;
        newNode.next = n.next;
        n.next.prev = newNode;
        n.next = newNode;
        if(n == tail){
            head = newNode;
        }
    }

    public void insertBeginning(Point p) {

        if (head == null){
            insertHead(p);
        }
        else if (tail == null){
            insertTail(p);
        }
        else {
            Node node = new Node(p);
            insertAfter(node, tail);
        }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        float total = 0;

        Point first = null;
        Point prev = null;
        for (Point p : this) {
            if(prev != null){
                total += distanceBetween(prev, p);
                prev = p;
            }
            else{
                first = prev = p;
            }
        }
        if(first != null && prev != null){
            total += distanceBetween(prev, first);
        }

        return total;
    }

    public void insertNearest(Point p) {

        if (head == null){
            insertHead(p);
        }
        else if (tail == null){
            Node node = new Node(p);
            tail = node;
            head.next = head.prev = tail;
            tail.next = tail.prev = head;
        }
        else {
            Node current = head;
            Node minNode = null;
            float min = Float.MAX_VALUE;

            while(current != tail){
                float dist = distanceBetween(p, current.point);
                if(dist < min){
                    min = dist;
                    minNode = current;
                }
                current = current.next;
            }
            float dist = distanceBetween(p, tail.point);
            if(dist < min){
                minNode = current;
            }
            Node newNode = new Node(p);
            newNode.prev = minNode;
            newNode.next = minNode.next;
            minNode.next.prev = newNode;
            minNode.next = newNode;
            if(minNode == tail){
                tail = newNode;
            }
        }
    }

    public void insertSmallest(Point p) {

        if (head == null){
            insertHead(p);
        }
        else if (tail == null){
            Node node = new Node(p);
            tail = node;
            head.next = head.prev = tail;
            tail.next = tail.prev = head;
        }
        else {
            Node current = head;
            Node insertNode = null;
            float min = Float.MAX_VALUE;

            while(current != tail){
                float distNew = distanceBetween(p, current.point) + distanceBetween(p, current.next.point);
                float distOld = distanceBetween(current.point, current.next.point);
                float change = distNew - distOld;
                if(change < min){
                    min = change;
                    insertNode = current;
                }
                current = current.next;
            }
            float distNew = distanceBetween(p, tail.point) + distanceBetween(p, tail.next.point);
            float distOld = distanceBetween(tail.point, tail.next.point);
            float change = distNew - distOld;
            if(change < min){
                insertNode = current;
            }
            Node newNode = new Node(p);
            newNode.prev = insertNode;
            newNode.next = insertNode.next;
            insertNode.next.prev = newNode;
            insertNode.next = newNode;
            if(insertNode == tail){
                tail = newNode;
            }
        }

    }

    public void reset() {
        head = null;
        tail = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
