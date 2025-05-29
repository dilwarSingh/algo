public static void debug(Object... objs) {
    for (int j = 0; j < objs.length; j++) {
        Object o = objs[j];
        if (o.getClass().isArray()) {
            System.out.print('[');
            int len = java.lang.reflect.Array.getLength(o);
            for (int i = 0; i < len; i++) {
                System.out.print(java.lang.reflect.Array.get(o, i));
                if (i != len - 1)
                    System.out.print(",");
            }
            System.out.print(']');
        } else
            System.out.print(o);
        if (j != objs.length - 1)
            System.out.print(new String(new char[] { ' ', '|', ' ' }));
    }
    System.out.println();
}
