package models;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class SearchResult implements Serializable {

	@SerializedName("from_user") public String from;
	@SerializedName("created_at") public Date date;
	@SerializedName("text") public String text;
}
