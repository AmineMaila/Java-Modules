package fr.forty_two.sockets.dto;

public class MessageDTO {
    private final String content;
    private final long fromId;
    private final long roomId;

    public MessageDTO(String content, long fromId, long roomId) {
        this.content = content;
        this.fromId = fromId;
        this.roomId = roomId;
    }

    public String toJson() {
        return String.format("{\"message\":\"%s\",\"fromId\":%d,\"roomId\":%d}", content, fromId, roomId);
    }

    public String getContent() { return content; }
    public long getFromId() { return fromId; }
    public long getRoomId() { return roomId; }
}
