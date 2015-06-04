package com.marengga.whizzle.adapter;

import com.marengga.whizzle.utils.Constant;
import com.marengga.whizzle.utils.CustomClickableSpan;
import com.marengga.whizzle.utils.CustomTagHandler;
import com.marengga.whizzle.utils.ExpandableTextView;
import com.marengga.whizzle.utils.FeedImageView;
import com.marengga.whizzle.R;
import com.marengga.whizzle.app.AppController;
import com.marengga.whizzle.data.NewsfeedModel;
 
import java.util.List;
 

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
 





import android.widget.TextView.BufferType;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
 
public class NewsfeedAdapter extends BaseAdapter {  
    private Activity activity;
    private LayoutInflater inflater;
    private List<NewsfeedModel> feedItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();
 
    public NewsfeedAdapter(Activity activity, List<NewsfeedModel> feedItems) {
        this.activity = activity;
        this.feedItems = feedItems;
    }
 
    @Override
    public int getCount() {
        return feedItems.size();
    }
 
    @Override
    public Object getItem(int location) {
        return feedItems.get(location);
    }
 
    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public int getViewTypeCount() {
        return 2;
    }
    
    @Override
    public int getItemViewType(int position) {
        return feedItems.get(position).getCategory();
    }
    	
	@Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	int category = feedItems.get(position).getCategory();
    	
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if (convertView == null){
            if (category == Constant.CAT_NEWS) {
            	convertView = inflater.inflate(R.layout.newsfeed_item_news, parent, false);
            }
            else {
            	convertView = inflater.inflate(R.layout.newsfeed_item_updates, parent, false);
            }
        }
 
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
 
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
        TextView newsContent = (TextView) convertView.findViewById(R.id.txtNewsContent);        
        TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
        FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);
 
        NewsfeedModel item = feedItems.get(position);
 
        title.setText(item.getTitle());
 
        // Converting timestamp into x ago format
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
                Long.parseLong(item.getTimeStamp()),
                System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        timestamp.setText(timeAgo);
 
        // Chcek for empty status message
        if (!TextUtils.isEmpty(item.getNewsContent())) {
            newsContent.setText(Html.fromHtml(item.getNewsContent().replace("\r\n",""), null, new CustomTagHandler()));
            newsContent.setVisibility(View.VISIBLE);
        } else {
            // status is empty, remove from view
        	newsContent.setVisibility(View.GONE);
        }
        // makeTextViewResizable(newsContent, 3, "View More", true);
 
        // Checking for null feed url
        if (item.getUrl() != null) {
            url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
                    + item.getUrl() + "</a> "));
 
            // Making url clickable
            url.setMovementMethod(LinkMovementMethod.getInstance());
            url.setVisibility(View.VISIBLE);
        } else {
            // url is null, remove from the view
            url.setVisibility(View.GONE);
        }
        
        if(item.getCategory() == Constant.CAT_UPDATE){
	        // user profile pic
        	NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.profilePic);
	        profilePic.setImageUrl(item.getProfilePic(), imageLoader);
        }
 
        // Feed image
        if (item.getImage() != null) {
            feedImageView.setImageUrl(item.getImage(), imageLoader);
            feedImageView.setVisibility(View.VISIBLE);
            feedImageView
                    .setResponseObserver(new FeedImageView.ResponseObserver() {
                        @Override
                        public void onError() {
                        }
 
                        @Override
                        public void onSuccess() {
                        }
                    });
        } else {
            feedImageView.setVisibility(View.GONE);
        }
 
        return convertView;
    }
	
	public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
            final int maxLine, final String spanableText, final boolean viewMore) {
        
    	String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new CustomClickableSpan(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "View Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "View More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
 
}