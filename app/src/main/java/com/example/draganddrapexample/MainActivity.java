package com.example.draganddrapexample;

import android.content.ClipData;
import android.content.ClipDescription;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.item1) TextView item1;
    @Bind(R.id.item2) TextView item2;
    @Bind(R.id.item3) TextView item3;
    @Bind(R.id.listView) ListView listView;
    ArrayAdapter<String> adapter;

    View.OnLongClickListener longClickListener = (view) -> {
        String tag = (String) view.getTag();
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
        ClipData.Item item = new ClipData.Item(tag);
        ClipData dragData = new ClipData(tag, new String[]{ClipDescription.MIMETYPE_TEXT_PLAIN}, item);
        view.startDrag(dragData, shadowBuilder, view, 0);
        return true;
    };

    View.OnDragListener dragListener = (view, event) -> {
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                return true;
            case DragEvent.ACTION_DROP:
                ClipData.Item item = event.getClipData().getItemAt(0);
                String text = item.getText().toString();
                int x = (int) event.getX();
                int y = (int) event.getY();
                int position = listView.pointToPosition(x, y);
                if (position == ListView.INVALID_POSITION) {
                    adapter.add(text);
                } else {
                    adapter.insert(text, position);
                }
                return true;
        }
        return false;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setAdapterInListView();
        //setItem(longClickListener, Arrays.asList(item1, item2, item3), Arrays.asList("item1", "item2", "item3"));
        setItem(longClickListener, item1, "item1");
        setItem(longClickListener, item2, "item2");
        setItem(longClickListener, item3, "item3");

        setOnDragListener(listView);
    }

    private void setAdapterInListView() {
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);
    }

    // 간편하지만 이 메소드를 textview 갯수마다 호출해야 한다
    private void setItem(View.OnLongClickListener longClickListener, TextView items, String itemTags) {
        items.setTag(itemTags);
        items.setOnLongClickListener(longClickListener);
    }

    // 스트림 api에서 두 컬랙션을 1:1 매핑시켜주는 것이 힘듬 --> 반복자 패턴 써서 해결해야함
    // flatMap이 1:1매핑이 아니라 1:리스트 전체 의 매핑임
    // 해결하려면? TextView와 String을 하나로 합쳐 새로운 자료형을 만든 후, 새 자료형 리스트에서 스트림을 사용해주면 된다
    /*private void setItem(View.OnLongClickListener longClickListener, List<TextView> items, List<String> itemTags) {
        Log.e("Stss", "sss");
        Stream.of(items).flatMap(item -> {
            return Stream.of(itemTags).map(itemTag -> {
                item.setTag(itemTag);
                item.setOnLongClickListener(longClickListener);
                Log.e("St", "dss");
                return item;
            });
        });
    }*/

    private void setOnDragListener(ListView listView) {
        listView.setOnDragListener(dragListener);
    }
}
