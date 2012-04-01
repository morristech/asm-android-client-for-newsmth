package com.athena.asm.Adapter;

import java.util.ArrayList;
import java.util.List;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.athena.asm.HomeActivity;
import com.athena.asm.PostListActivity;
import com.athena.asm.R;
import com.athena.asm.data.Attachment;
import com.athena.asm.data.Post;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class PostListAdapter extends BaseAdapter {

	private PostListActivity activity;
	private LayoutInflater inflater;
	private List<Post> postList;
	
	public class ViewHolder {
		public TextView authorTextView;
		public TextView titleTextView;
		public TextView contentTextView;
		public TextView attachTextView;
		public LinearLayout imageLayout;
		public TextView dateTextView;
		public Post post;
	}

	public PostListAdapter(PostListActivity activity, LayoutInflater inflater,
			List<Post> postList) {
		this.activity = activity;
		this.inflater = inflater;
		this.postList = postList;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		RelativeLayout layout = null;
		Post post = postList.get(position);
		
		if (convertView == null) {
			layout = (RelativeLayout) inflater.inflate(R.layout.post_list_item, null);
			holder = new ViewHolder();
			holder.authorTextView = (TextView) layout.findViewById(R.id.AuthorID);
			holder.titleTextView = (TextView) layout.findViewById(R.id.PostTitle);
			holder.contentTextView = (TextView) layout.findViewById(R.id.PostContent);
			holder.attachTextView = (TextView) layout.findViewById(R.id.PostAttach);
			holder.imageLayout = (LinearLayout) layout.findViewById(R.id.imageLayout);
			holder.dateTextView = (TextView) layout.findViewById(R.id.PostDate);
			holder.post = post;
			
			holder.contentTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP,
					HomeActivity.application.getPostFontSize());
			
			layout.setTag(holder);
		}
		else {
			layout = (RelativeLayout)convertView;
			holder = (ViewHolder)layout.getTag();
		}		
		
		if (post.getAuthor() == null) {
			holder.titleTextView.setText("错误的文章号,原文可能已经被删除");
			return layout;
		}
		
		holder.authorTextView.setText(post.getAuthor());
		holder.titleTextView.setText(post.getTitle());
		
		holder.contentTextView.setText(post.getContent());
		holder.attachTextView.setMovementMethod(LinkMovementMethod.getInstance());
		ArrayList<Attachment> attachments = post.getAttachFiles();
		if (attachments != null) {
			holder.imageLayout.removeAllViews();
			StringBuilder contentBuilder = new StringBuilder();
			contentBuilder.append("");

			for (int i = 0; i < attachments.size(); i++) {
				String attachUrl = attachments.get(i).getAttachUrl();
				contentBuilder.append("<a href='").append(attachUrl).append("'>");
				contentBuilder.append(attachments.get(i).getName()).append("</a><br/><br/>");
				String fileType = attachments.get(i).getName().toLowerCase();
				if (fileType.endsWith("jpg")
						|| fileType.endsWith("jpeg")
						|| fileType.endsWith("png") || fileType.endsWith("bmp")
						|| fileType.endsWith("gif")) {
					// Log.d("image", attachUrl);
					ImageView imageView = new ImageView(activity);
					LayoutParams layoutParams = new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					imageView.setLayoutParams(layoutParams);
					holder.imageLayout.addView(imageView);
					UrlImageViewHelper.setUrlDrawable(imageView, attachUrl,
							R.drawable.loading, 60000);
				}
			}
			holder.attachTextView.setText(Html.fromHtml(contentBuilder.toString()));
		}

		holder.dateTextView.setText(post.getDate().toLocaleString());

		holder.contentTextView.setOnLongClickListener(activity);
		layout.setOnLongClickListener(activity);
		
		holder.contentTextView.setOnTouchListener(activity);
		layout.setOnTouchListener(activity);
		
		if (HomeActivity.application.isNightTheme()) {
			holder.titleTextView.setTextColor(layout.getResources().getColor(R.color.status_text_night));
			holder.contentTextView.setTextColor(layout.getResources().getColor(R.color.status_text_night));
			holder.attachTextView.setTextColor(layout.getResources().getColor(R.color.status_text_night));
			holder.authorTextView.setTextColor(layout.getResources().getColor(R.color.blue_text_night));
		}

		return layout;
	}

	@Override
	public int getCount() {
		return postList.size();
	}

	@Override
	public Object getItem(int position) {
		if (position >= postList.size()) {
			position = postList.size() - 1;
		}
		return postList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
