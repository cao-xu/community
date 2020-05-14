package club.sword.community.enums;

/**
 * Created by XuCao on 2020/5/14
 */
public enum NotificationStatusEnum {
    UNREAD(0), READ(1);

    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
