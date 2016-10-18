/*
 * Copyright (c) 2013 Bull SAS.
 * All rights reserved.
 */
package fr.csmb.competition.network.receiver;

import fr.csmb.competition.xml.model.Club;
import fr.csmb.competition.xml.model.Competition;

/**
 * DatagramListener.
 *
 * @author Bull SAS
 * @requirement SICS-407
 */
public interface DatagramListener {

    public void receive(Competition competition);

    public void receive(Club club);
}
