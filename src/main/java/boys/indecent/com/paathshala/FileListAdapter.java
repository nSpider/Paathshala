package boys.indecent.com.paathshala;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by nSpider on 29-Jul-17.
 */

public class FileListAdapter extends ArrayAdapter<FileUpload> {
    private Activity context;
    private int resource;
    private List<FileUpload> listFile;


    public FileListAdapter(@NonNull Activity context, @LayoutRes int resource, @NonNull List<FileUpload> objects) {
        super(context, resource, objects);
        this.context=context;
        this.resource = resource;
        listFile=objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View v = inflater.inflate(resource,null);
        TextView tvName = (TextView) v.findViewById(R.id.FileName);
        TextView dLink = (TextView) v.findViewById(R.id.dLink);

        tvName.setText(listFile.get(position).getName());
        dLink.setText(String.format("Download link: %s", listFile.get(position).getUrl()));

        return v;
    }
}
