/*
 * Autopsy Forensic Browser
 *
 * Copyright 2011 Basis Technology Corp.
 * Contact: carrier <at> sleuthkit <dot> org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.sleuthkit.autopsy.ingest;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.sleuthkit.autopsy.datamodel.KeyValue;
import org.sleuthkit.datamodel.BlackboardArtifact;

/**
 * Representation of subject posted by ingest services
 * 
 * Message should have a unique ID within context of originating source
 * 
 */
public class IngestMessage {

    public enum MessageType {

        DATA, INFO, WARNING, ERROR
    };
    private long ID;
    private MessageType messageType;
    private IngestServiceAbstract source;
    private String subject;
    private String detailsHtml;
    private String uniqueKey;
    private BlackboardArtifact data;
    private Date datePosted;
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private IngestMessage(long ID, MessageType messageType, IngestServiceAbstract source, String subject, String detailsHtml) {
        this.ID = ID;
        this.source = source;
        this.messageType = messageType;
        this.subject = subject;
        this.detailsHtml = detailsHtml;
        datePosted = new Date();
    }

    //getters
    public long getID() {
        return ID;
    }

    public IngestServiceAbstract getSource() {
        return source;
    }

    public String getSubject() {
        return subject;
    }

    public String getDetails() {
        return detailsHtml;
    }

    public String getUniqueKey() {
        return uniqueKey;
    }

    public BlackboardArtifact getData() {
        return data;
    }

    public MessageType getMessageType() {
        return messageType;
    }

    public Date getDatePosted() {
        return datePosted;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Long.toString(ID)).append(": ");
        sb.append("type: ").append(messageType.name());
        if (source != null) //can be null for manager messages
        {
            sb.append(" source: ").append(source.getName());
        }
        sb.append(" date: ").append(dateFormat.format(datePosted));
        sb.append(" subject: ").append(subject);
        if (detailsHtml != null) {
            sb.append(" details: ").append(detailsHtml);
        }
        if (data != null) {
            sb.append(" data: ").append(data.toString()).append(' ');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final IngestMessage other = (IngestMessage) obj;
        if (this.ID != other.ID) {
            return false;
        }
        if (this.messageType != other.messageType) {
            return false;
        }
        if (this.source != other.source && (this.source == null || !this.source.equals(other.source))) {
            return false;
        }
        if ((this.subject == null) ? (other.subject != null) : !this.subject.equals(other.subject)) {
            return false;
        }
        if ((this.detailsHtml == null) ? (other.detailsHtml != null) : !this.detailsHtml.equals(other.detailsHtml)) {
            return false;
        }
        if ((this.uniqueKey == null) ? (other.uniqueKey != null) : !this.uniqueKey.equals(other.uniqueKey)) {
            return false;
        }
        if (this.data != other.data && (this.data == null || !this.data.equals(other.data))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (int) (this.ID ^ (this.ID >>> 32));
        hash = 59 * hash + (this.messageType != null ? this.messageType.hashCode() : 0);
        hash = 59 * hash + (this.source != null ? this.source.hashCode() : 0);
        hash = 59 * hash + (this.subject != null ? this.subject.hashCode() : 0);
        hash = 59 * hash + (this.detailsHtml != null ? this.detailsHtml.hashCode() : 0);
        hash = 59 * hash + (this.uniqueKey != null ? this.uniqueKey.hashCode() : 0);
        hash = 59 * hash + (this.data != null ? this.data.hashCode() : 0);
        return hash;
    }

    //factory methods
    /**
     * Create a simple message with a subject only
     * @param ID ID of the message, unique in the context of module that generated it
     * @param messageType message type
     * @param source originating service
     * @param subject message subject to be displayed
     * @param details message details to be displayed, or null
     * @return 
     */
    public static IngestMessage createMessage(long ID, MessageType messageType, IngestServiceAbstract source, String subject, String detailsHtml) {
        if (messageType == null || source == null || subject == null) {
            throw new IllegalArgumentException("message type, source and subject cannot be null");
        }
        return new IngestMessage(ID, messageType, source, subject, detailsHtml);
    }

    /**
     * Create a simple message with a subject only
     * @param ID ID of the message, unique in the context of module that generated it
     * @param messageType message type
     * @param source originating service
     * @param subject message subject to be displayed
     * @return 
     */
    public static IngestMessage createMessage(long ID, MessageType messageType, IngestServiceAbstract source, String subject) {
        return createMessage(ID, messageType, source, subject, null);
    }

    /**
     * Create error message
     * @param ID ID of the message, unique in the context of module that generated it
     * @param source originating service
     * @param subject message subject to be displayed
     * @return 
     */
    public static IngestMessage createErrorMessage(long ID, IngestServiceAbstract source, String subject) {
        if (source == null || subject == null) {
            throw new IllegalArgumentException("source and subject cannot be null");
        }
        return new IngestMessage(ID, MessageType.ERROR, source, subject, null);
    }
    
    /**
     * Create warning message
     * @param ID ID of the message, unique in the context of module that generated it
     * @param source originating service
     * @param subject message subject to be displayed
     * @return 
     */
    public static IngestMessage createWarningMessage(long ID, IngestServiceAbstract source, String subject) {
        if (source == null || subject == null) {
            throw new IllegalArgumentException("source and subject cannot be null");
        }
        return new IngestMessage(ID, MessageType.WARNING, source, subject, null);
    }

    /**
     * 
     * @param ID ID of the message, unique in the context of module that generated it
     * @param source originating service
     * @param subject message subject to be displayed
     * @param detailsHtml html formatted detailed message (without leading and closing <html> tags), for instance, a human-readable representation of the data. 
     * @param uniqueKey unique key determining uniqueness of the message, or null. Helps grouping similar messages and determine their importance.  Subsequent messages with the same uniqueKey will be treated with lower priority.
     * @param data data blackboard artifact associated with the message, the same as fired in ServiceDataEvent by the service
     * @return 
     */
    public static IngestMessage createDataMessage(long ID, IngestServiceAbstract source, String subject, String detailsHtml, String uniqueKey, BlackboardArtifact data) {
        if (source == null || subject == null || detailsHtml == null || data == null) {
            throw new IllegalArgumentException("source, subject, details and data cannot be null");
        }

        IngestMessage im = new IngestMessage(ID, MessageType.DATA, source, subject, detailsHtml);
        im.uniqueKey = uniqueKey;
        im.data = data;
        return im;
    }

    static IngestMessage createManagerMessage(String subject) {
        return new IngestMessage(0, MessageType.INFO, null, subject, null);
    }
}
