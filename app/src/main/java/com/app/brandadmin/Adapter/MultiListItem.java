package com.app.brandadmin.Adapter;
public class MultiListItem {

    public static final int LAYOUT_RECOMMANDATION = 1;
    public static final int LAYOUT_COLOR = 2;
    public static final int LAYOUT_FAQ = 3;
    public static final int LAYOUT_HINDI = 4;
    public static final int LAYOUT_MYBUSINESS = 5;
    public static final int LAYOUT_AATMNIRBHARTA = 6;
    public static final int LAYOUT_BUSINESSETHIC = 7;
    public static final int LAYOUT_GOODMORNING = 8;
    public static final int LAYOUT_MOTIVATION = 9;
    public static final int LAYOUT_LEADERSQUOTES = 10;
    public static final int LAYOUT_DEVOTIONAL = 11;
    public static final int LAYOUT_TOPCHART = 12;
    public static final int LAYOUT_UPCOMINGFESTIVAL = 13;
    public static final int ACTIVITY_VIEWALLIMAGE=22;
    public static final int ACTIVITY_VIEWALLFRAME=23;
    public static final int LAYOUT_LOADING = 33;
    private int layoutType;
    private int Image;
    private String Question;
    private String Answer;



    public MultiListItem() {
    }

    public static int getLayoutRecommandation() {
        return LAYOUT_RECOMMANDATION;
    }
    public static int getLayoutGujrati() { return LAYOUT_COLOR; }
    public static int getLayoutEnglish() { return LAYOUT_FAQ; }
    public static int getLayoutHindi() { return LAYOUT_HINDI; }
    public static int getLayoutMybusiness() { return LAYOUT_MYBUSINESS; }
    public static int getLayoutAatmnirbharta() { return LAYOUT_AATMNIRBHARTA; }
    public static int getLayoutBusinessethic() { return LAYOUT_BUSINESSETHIC; }
    public static int getLayoutGoodmorning() { return LAYOUT_GOODMORNING; }
    public static int getLayoutMotivation() { return LAYOUT_MOTIVATION; }
    public static int getLayoutLeadersquotes() { return LAYOUT_LEADERSQUOTES; }
    public static int getLayoutDevotional() { return LAYOUT_DEVOTIONAL; }
    public static int getLayoutTopchart() { return LAYOUT_TOPCHART; }
    public static int getLayoutUpcomingfestival() { return LAYOUT_UPCOMINGFESTIVAL; }

    public static int getActivityViewallimage() {
        return ACTIVITY_VIEWALLIMAGE;
    }

    public static int getLayoutLoading() {
        return LAYOUT_LOADING;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getAnswer() {
        return Answer;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }
}
