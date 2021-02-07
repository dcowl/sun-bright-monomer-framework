package org.sun.bright.framework.id.uuid;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 根据JDK生成UUID UUID 的目的是让分布式系统中的所有元素，都能有唯一的辨识资讯，而不需要透过中央控制端来做辨识资讯的指定。
 * 如此一来，每个人都可以建立不与其它人冲突的 UUID。在这样的情况下，就不需考虑数据库建立时的名称重复问题。 目前最广泛应用的 UUID，即是微软的
 * Microsoft's Globally Unique Identifiers (GUIDs)，而其他重要的应用， 则有 Linux ext2/ext3
 * 档案系统、LUKS 加密分割区、GNOME、KDE、Mac OS X 等等。
 *
 * @author <a href="mailto:2867665887@qq.com">dcowl</a>
 * @version 1.0
 */
public class UUIDUtils {

    private UUIDUtils() {
    }

    /**
     * getUUID: 获得指定数目的UUID
     *
     * @param number int 需要获得的UUID数量
     * @return String[] UUID数组
     */
    public static synchronized String[] getUuid(int number) {
        if (number < 1) {
            return new String[0];
        }
        String[] str = new String[number];
        for (int i = 0; i < number; i++) {
            str[i] = getUuid();
        }
        return str;
    }

    /**
     * getUUID 获得一个不带"-"UUID
     *
     * @return String UUID
     */
    public static synchronized String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    private static final String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "0", "1", "2", "3",
            "4", "5", "6", "7", "8", "9"};

    /**
     * 获取长度为8为的数字
     *
     * @return String UUID
     */
    public static synchronized String getShortUuid() {
        // 生成UUID
        String uuid = UUID.randomUUID().toString().replace("-", "");
        // 32 -> 8
        int generateLength = 8;
        // StringBuffer实例
        AtomicReference<StringBuilder> atomicStringBuilder = new AtomicReference<>(new StringBuilder());
        for (int i = 0; i < generateLength; i++) {
            //截取字符
            String str = uuid.substring(i * 4, i * 4 + 4);
            //16进制为基解析
            int strInteger = Integer.parseInt(str, 16);
            //0x3E->字典总数 62
            atomicStringBuilder.get().append(chars[strInteger % 0x3E]);
        }
        return atomicStringBuilder.toString();
    }

}
