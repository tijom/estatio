package com.eurocommercialproperties.estatio.viewer.wicket.app;

import com.google.inject.Singleton;

import org.apache.isis.viewer.wicket.ui.components.collectioncontents.fullcalendar.CollectionContentsAsFullCalendarFactory;
import org.apache.isis.viewer.wicket.viewer.registries.components.ComponentFactoryRegistrarDefault;

@Singleton
public class ComponentFactoryRegistrarForEstatio extends ComponentFactoryRegistrarDefault {

    @Override
    public void addComponentFactories(ComponentFactoryList componentFactories) {
        super.addComponentFactories(componentFactories);
        // currently no replacements
        componentFactories.add(new CollectionContentsAsFullCalendarFactory());
    }
}