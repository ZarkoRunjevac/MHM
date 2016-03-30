package zarkorunjevac.mhm.mhm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import zarkorunjevac.mhm.R;

public class TestActvity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout.LayoutParams params = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

// create layout
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

//create a button
        Button btn = new Button(this);
        btn.setText("Click Me");
        btn.setLayoutParams(params);

//create text view widget
        TextView tv = new TextView(this);
        tv.setText("Hello World!! AGAIN!!");
        tv.setLayoutParams(params);

//add button to layout
        layout.addView(btn);
        layout.addView(tv);
//add textview to layout
        ContentAdapter adapter = new ContentAdapter();
        List<RecyclerView> lista=new ArrayList<RecyclerView>();
        for(int i=0;i<3;i++){

            TextView tv1 = new TextView(this);
            tv1.setText("Hello World!! AGAIN!!");
            tv1.setLayoutParams(params);
            layout.addView(tv1);

            RecyclerView recyclerView=new RecyclerView(this);
            recyclerView.setLayoutParams(params);
            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            lista.add(i,recyclerView);
            layout.addView(recyclerView);

        }






// now create layout params
        LinearLayout.LayoutParams layoutParam = new
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT );

// now add the layout
        ScrollView m_Scroll;
        m_Scroll = new ScrollView(this);
        m_Scroll.addView(layout,layoutParam);

        this.addContentView(m_Scroll, layoutParam);


    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.item_list, parent, false));
        }
    }
    /**
     * Adapter to display recycler view.
     */
    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        // Set numbers of List in RecyclerView.
        private static final int LENGTH = 4;

        public ContentAdapter() {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            // no-op
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}
