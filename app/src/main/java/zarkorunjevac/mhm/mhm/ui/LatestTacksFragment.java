package zarkorunjevac.mhm.mhm.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import zarkorunjevac.mhm.R;
import zarkorunjevac.mhm.mhm.model.Blog;

/**
 * Created by zarko.runjevac on 3/21/2016.
 */
public class LatestTacksFragment extends Fragment {
    private List<Blog> blogs;
    public LatestTacksFragment(){

    }

    public void getBlogs(List<Blog> bl){
        this.blogs=bl;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView recyclerView = (RecyclerView) inflater.inflate(
                R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(blogs);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
            titleTextView=(TextView)itemView.findViewById(R.id.list_title);
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 18;
        private List<Blog> mBlogs;
        public ContentAdapter( List<Blog> blogs) {
            mBlogs=blogs;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
            Blog blog=mBlogs.get(position);
            TextView textView=holder.titleTextView;
            textView.setText(blog.getSitename());
        }

        @Override
        public int getItemCount() {
            return (mBlogs == null) ? 0 : mBlogs.size();
        }
    }
}