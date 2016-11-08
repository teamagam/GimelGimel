package com.teamagam.gimelgimel.app.view.fragments.viewer_footer_fragments;

import android.view.View;

import com.teamagam.gimelgimel.app.common.logging.LoggerFactory;
import com.teamagam.gimelgimel.domain.base.logging.Logger;

import java.util.Collection;

/**
 * Displays button with vector manipulation functionality
 */
public class VectorManipulationFooterFragment extends BaseButtonViewerFooterFragment
        implements View.OnClickListener {

    private static final Logger sLogger = LoggerFactory.create(
            VectorManipulationFooterFragment.class);

    @Override
    protected Collection<Integer> getButtonsIds() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected int getFragmentLayout() {
        return 0;
    }

/*
    private static final VectorLayer sVectorLayer;
    private static KMLLayer sKmlLayer;

    static {
        sVectorLayer = new VectorLayer(IdCreatorUtil.getUniqueId());
        sKmlLayer = new KMLLayer(IdCreatorUtil.getUniqueId(), "SampleData/kml/facilities.kml");
    }

    @Override
    protected Collection<Integer> getButtonsIds() {
        return Arrays.asList(
                R.id.vector_manipulation_add_button,
                R.id.vector_manipulation_update_button,
                R.id.vector_manipulation_delete_button,
                R.id.vector_manipulation_kml_button,
                R.id.vector_manipulation_delete_all_button
        );
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_manipulation_vector;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.vector_manipulation_add_button:
                addVectors();
                break;
            case R.id.vector_manipulation_delete_button:
                deleteRandomVector();
                break;
            case R.id.vector_manipulation_kml_button:
                addKmlLayer();
                break;
            case R.id.vector_manipulation_update_button:
                updateVectors();
                break;
            case R.id.vector_manipulation_delete_all_button:
                deleteAllVectorLayers();
                break;
            default:
                sLogger.e("Unsupported view was clicked!");
                break;
        }
    }

    private void deleteAllVectorLayers() {
        GGMap ggMap = getInterface().getGGMap();
        Collection<GGLayer> layers = ggMap.getLayers();

        //Avoids concurrent modification while iterating (cannot foreach)
        GGLayer[] layerArray = layers.toArray(new GGLayer[0]);
        for (int i = 0; i < layerArray.length; i++) {
            ggMap.removeLayer(layerArray[i].getId());
        }

        Entity[] entities = sVectorLayer.getEntities().toArray(new Entity[0]);
        for (int i = 0; i < entities.length; i++) {
            sVectorLayer.removeEntity(entities[i].getId());
        }
    }

    private void updateVectors() {
        if (getInterface().getGGMap().getLayer(sVectorLayer.getId()) == null) {
            Toast.makeText(getActivity(), "First, create a layer", Toast.LENGTH_SHORT).show();
            return;
        }

        Collection<Entity> vEntities = sVectorLayer.getEntities();

        EntitiesHelperUtils.randomlyUpdateEntities(vEntities);
    }

    private void addVectors() {
        ensureLayerInViewer(sVectorLayer);

        int radius = 1;
        double anchorLng = 34.8;
        double anchorLat = 32.2;

        Point p = EntitiesHelperUtils.generateRandomPoint(anchorLat, anchorLng, radius);
        Polyline pl = EntitiesHelperUtils.generateRandomPolyline(anchorLat, anchorLng, radius);
        Polygon polygon = EntitiesHelperUtils.generateRandomPolygon(anchorLat, anchorLng, radius);

        sVectorLayer.addEntity(p);
        sVectorLayer.addEntity(pl);
        sVectorLayer.addEntity(polygon);
    }

    private void deleteRandomVector() {
        Entity[] entities = sVectorLayer.getEntities().toArray(new Entity[0]);

        if (entities.length == 0) {
            Toast.makeText(getActivity(), "No entities to remove from vector layer",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Entity randEntity = entities[((int) Math.floor(Math.random() * entities.length))];

        sVectorLayer.removeEntity(randEntity.getId());
    }

    private void addKmlLayer() {
        ensureLayerInViewer(sKmlLayer);
    }

    private void ensureLayerInViewer(GGLayer vectorLayer) {
        if (vectorLayer == null) {
            throw new IllegalArgumentException("vectorLayer is null!");
        }

        GGMap ggMapView = getInterface().getGGMap();
        if (ggMapView.getLayer(vectorLayer.getId()) == null) {
            ggMapView.addLayer(vectorLayer);
        }
    }
*/
}
