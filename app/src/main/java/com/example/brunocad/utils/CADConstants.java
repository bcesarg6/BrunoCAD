package com.example.brunocad.utils;

public class CADConstants {

    public static class TabsID {
        public static final int CREATE = 1;
        public static final int TOOLS = 2;
        public static final int EDIT = 3;
    }

    public static class toolsID {
        public static final int HELP = -1;
        public static final int NONE = 0;

        //Editar
        public static final int CLEAR = 1;
        public static final int TRANSLATION = 2;
        public static final int ROTATION = 3;
        public static final int CHANGE_ESCALE = 4;
        public static final int ZOOM_EXTEND = 5;

        //Criar
        public static final int LINE = 6;

        public static final int TRIANGLE_STROKE = 7;
        public static final int TRIANGLE = 8;

        public static final int RECTANGLE_STROKE = 9;
        public static final int RECTANGLE = 10;

        public static final int CIRCLE_STROKE = 11;
        public static final int CIRCLE = 12;
    }

    public static class drawingTypes {
        public static final int LINE = 1;
        public static final int TRIANGLE = 2;
        public static final int RECTANGLE = 3;
        public static final int CIRCLE = 4;
    }

}
