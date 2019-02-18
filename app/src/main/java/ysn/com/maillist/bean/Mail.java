package ysn.com.maillist.bean;

/**
 * @Author yangsanning
 * @ClassName Mail
 * @Description 一句话概括作用
 * @Date 2019/2/18
 * @History 2019/2/18 author: description:
 */
public class Mail {

    private String name;

    /**
     * 拼音的首字母
     */
    private String letter;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }
}
