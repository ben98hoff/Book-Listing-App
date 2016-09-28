package com.waters89gmail.dave.booklistingapp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;
/**
 * Created by WatersD on 7/27/2016.
 */
public class BookAdapter extends ArrayAdapter<BookObject> {

    public BookAdapter(Context context, List<BookObject> objects) {
        super(context, 0, objects);
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.custom_list_view, parent, false);

            /*
            Using a view holder class so that it is only necessary to use find view by id when view is inflated.
             */
            viewHolder = new ViewHolder();

            /*
            Finding views and assigning to viewholders.
             */
            viewHolder.title = (TextView) listItemView.findViewById(R.id.title_text);
            viewHolder.author = (TextView) listItemView.findViewById(R.id.author_text);
            viewHolder.description = (TextView) listItemView.findViewById(R.id.description_text);
            viewHolder.thumbNail = (ImageView) listItemView.findViewById(R.id.thumbnail);
            viewHolder.progressBar = (ProgressBar) listItemView.findViewById(R.id.progress);

            /*
            setting a tag to the view for future reference instead of using find view by id each time.
             */
            listItemView.setTag(viewHolder);
        } else {

            /*
            here the tag is used to reference the view if it has already been inflated and a tag set to the holder.
             */
            viewHolder = (ViewHolder) listItemView.getTag();
        }

        /*
        Getting current object position in Array.
         */
        BookObject currentBookObject = getItem(position);

        /*
        Checking to see if object exists before setting data to viewHolders.
         */
        if (currentBookObject != null) {

            viewHolder.title.setText(currentBookObject.getTitle());

            /*
            Adding prefix (Author: ) to author string before setting string to viewHolder.
             */
            String author = getContext().getString(R.string.by)+" "+currentBookObject.getAuthor();
            viewHolder.author.setText(author);

            /*
            Checking if description exists for currentBookObject.  If none exists setting description viewHolder to default of
            * No description available. and reducing text size.
             */
            String description;
            if(currentBookObject.getDescription()==null || currentBookObject.getDescription().equals("")){
                viewHolder.description.setTextSize(8);
                description = "\n"+getContext().getResources().getString(R.string.no_description);
            }else {
                viewHolder.description.setTextSize(12);
                description = currentBookObject.getDescription();
            }
            viewHolder.description.setText(description);

            /*
            Using Glide to smooth out loading and setting of image to listview.
            added (com.github.bumptech.glide:glide:3.7.0') to build.gradle file.
             */
            if (!currentBookObject.getThumbNailUrl().equals("")){

                viewHolder.progressBar.setVisibility(View.VISIBLE);
                Glide.with(getContext())
                        .load(currentBookObject.getThumbNailUrl())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(viewHolder.thumbNail);
                //Glide.with(this.getContext()).load(currentBookObject.getThumbNailUrl()).into(viewHolder.thumbNail).onLoadStarted(getContext().getDrawable(R.drawable.stub));
            }else{
                viewHolder.progressBar.setVisibility(View.GONE);
                /*
            Setting book image to a default stub image before checking for and setting thumbnail image.  Default is for case
            where no image is available.
             */
                viewHolder.thumbNail.setImageResource(R.drawable.stub);
            }
        }
        return listItemView;
    }

    static class ViewHolder {

        TextView title;
        TextView author;
        TextView description;
        ImageView thumbNail;
        ProgressBar progressBar;
    }

}
