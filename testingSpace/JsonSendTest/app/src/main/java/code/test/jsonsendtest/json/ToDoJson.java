package code.test.jsonsendtest.json;

import com.google.gson.JsonArray;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.time.LocalDateTime;
import java.util.List;

public class ToDoJson {
    @SerializedName("seq")
    private Long seq;
    @SerializedName("save")
    private String save;
    @SerializedName("loginId")
    private String loginId;
    @SerializedName("json")
    private JsonArray json;
    @SerializedName("modDate")
    private String modDate;

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public String getModDate() {
        return modDate;
    }

    public void setModDate(String modDate) {
        this.modDate = modDate;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public JsonArray getJson() {
        return json;
    }

    public void setJson(JsonArray json) {
        this.json = json;
    }
}
