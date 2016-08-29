package com.teamagam.gimelgimel.app.message.viewModel;

import android.content.Context;
import android.databinding.BaseObservable;

import com.teamagam.gimelgimel.R;
import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.app.map.model.geometries.PointGeometry;
import com.teamagam.gimelgimel.app.network.services.GGMessageSender;
import com.teamagam.gimelgimel.domain.base.logging.Logger;
import com.teamagam.gimelgimel.presentation.presenters.SendGeoMessagePresenter;
import com.teamagam.gimelgimel.presentation.scopes.PerFragment;

import javax.inject.Inject;

/**
 * View Model that handles send geographic messages from user in
 * {@link SendGeoMessageViewModel}.
 * <p/>
 * Controls communication between presenter and view.
 */
@PerFragment
public class SendGeoMessageViewModel extends BaseObservable {

    private Logger sLogger = LoggerFactory.create(this.getClass());

    public String[] types;

    private String text;
    private PointGeometry point;
    private int typeIdx;

    private ISendGeoMessageView mView;

    @Inject
    SendGeoMessagePresenter mPresenter;

    @Inject
    Context context;

    @Inject
    public SendGeoMessageViewModel() {
        super();
    }

    public void init(ISendGeoMessageView view, PointGeometry point) {
        this.types = context.getResources().getStringArray(R.array.geo_locations_types);
        this.point = point;
        this.mView = view;
    }

    public void clickedOK() {
        if (isInputValid()) {
            mPresenter.sendGeoMessage(GGMessageSender.getUserName(context), text, point.latitude,
                    point.longitude, point.altitude, types[typeIdx]);
            mView.dismiss();
        } else {
            //validate that the user has entered description
            sLogger.userInteraction("Input not valid");
            mView.showError();
        }
    }

    private boolean isInputValid() {
        return text != null && !text.isEmpty();
    }

    public PointGeometry getPoint() {
        return point;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setTypeIdx(int type) {
        this.typeIdx = type;
    }

    public int getTypeIdx() {
        return typeIdx;
    }

    public interface ISendGeoMessageView {
        void showError();

        void dismiss();
    }

}
