package io.github.yehiareda.shapecontainer.sample;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MaterialDesignShrineActivity extends AppCompatActivity {

    View menu;
    View bottomSheet;
    View middleSheet;
    View topSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_of_view_activity_main_shrine);
        menu = findViewById(R.id.menu);
        bottomSheet = findViewById(R.id.bottomSheet);
        middleSheet = findViewById(R.id.middleSheet);
        topSheet = findViewById(R.id.topSheet);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onMenuClicked();
            }
        });
    }

    public void onMenuClicked() {
        final boolean enabled = middleSheet.isEnabled();
        if (enabled) {
            middleSheet.animate().translationY(bottomSheet.getHeight());
        } else {
            middleSheet.animate().translationY(0);
        }
        middleSheet.setEnabled(!enabled);
    }

    void onTopSheetClicked() {

    }

    void onAddToCartClicked() {

    }
}
