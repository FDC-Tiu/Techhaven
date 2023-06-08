package com.example.techhaven.ui.comment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.example.techhaven.Comment;
import com.example.techhaven.CommentAdapter;
import com.example.techhaven.R;
import com.example.techhaven.VolleySingleton;
import com.example.techhaven.databinding.FragmentCommentBinding;
import com.example.techhaven.ui.webview.WebViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    private List<Comment> commentList;
    private CommentAdapter adapter;
    private RecyclerView recyclerView;
    private Button shopeeBtn;
    private Button lazadaBtn;
    private WebView webView;

    private void loadWebsite(String url) {
        webView.loadUrl(url);
    }

    private void setupButtons() {
        Button shopeeBtn = getView().findViewById(R.id.shopee_btn);
        Button lazadaBtn = getView().findViewById(R.id.lazada_btn);

        shopeeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWebsite("www.shopee.com");
            }
        });

        lazadaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadWebsite("www.lazada.com");
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call the setupButtons method
        setupButtons();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CommentViewModel dashboardViewModel =
                new ViewModelProvider(this).get(CommentViewModel.class);

        binding = FragmentCommentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerView);
        commentList = new ArrayList<>();
        shopeeBtn = root.findViewById(R.id.shopee_btn);
        lazadaBtn = root.findViewById(R.id.lazada_btn);
        webView = root.findViewById(R.id.webview_container);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new WebViewClient());

        RecyclerView recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getComments();

        return root;
    }

    public void getComments() {

        String url = "https://dummyjson.com/comments";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        Log.e("Products: ", response);
                        JSONObject jsonObject = new JSONObject(response);

                        JSONArray jsonArray = jsonObject.getJSONArray("comments");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            Comment comment = new Comment();
                            comment.setBody(object.getString("body"));

                            JSONObject userObject = object.getJSONObject("user");
                            comment.setAuthor(userObject.getString("username"));
                            commentList.add(comment);
                        }
                        adapter = new CommentAdapter(getContext(), commentList);
                        recyclerView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "Failed" + e, Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(getContext(), "Failed" + error, Toast.LENGTH_SHORT).show();
                });
        VolleySingleton.getmInstance().addToRequestQueue(stringRequest);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Hide the toolbar back button icon
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // Add the onOptionsItemSelected method here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Handle the back button click
            requireActivity().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

