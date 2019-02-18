package ysn.com.maillist.utils;

import java.util.Comparator;

import ysn.com.maillist.bean.Mail;

/**
 * @Author yangsanning
 * @ClassName PinyinComparator
 * @Description 一句话概括作用
 * @Date 2019/2/18
 * @History 2019/2/18 author: description:
 */
public class PinyinComparator implements Comparator<Mail> {

    @Override
    public int compare(Mail o1, Mail o2) {
        if ("@".equals(o1.getLetter()) || "#".equals(o2.getLetter())) {
            return -1;
        } else if ("#".equals(o1.getLetter()) || "@".equals(o2.getLetter())) {
            return 1;
        } else {
            return o1.getLetter().compareTo(o2.getLetter());
        }
    }
}