/*
 * 
 * Autopsy Forensic Browser
 * 
 * Copyright 2018 Basis Technology Corp.
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
package org.sleuthkit.autopsy.commonfilesearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttribute;
import org.sleuthkit.autopsy.centralrepository.datamodel.CorrelationAttributeInstance;
import org.sleuthkit.autopsy.datamodel.DisplayableItemNode;
import org.sleuthkit.datamodel.AbstractFile;

/**
 * Generates a DisplayableItmeNode using a CentralRepositoryFile.
 */
final public class CentralRepositoryCaseFileInstanceMetadata extends FileInstanceNodeGenerator {
    
    private final Integer crFileId;
    private CorrelationAttributeInstance tempAttributeInst;
    
    CentralRepositoryCaseFileInstanceMetadata(Integer attrInstId, Map<Long, AbstractFile> cachedFiles) {
        super(cachedFiles);
        this.crFileId = attrInstId;
    }
    
    @Override
    public DisplayableItemNode generateNode() {
        if (tempAttributeInst != null) {
            return new CentralRepositoryFileInstanceNode(tempAttributeInst, this.lookupOrCreateAbstractFile());
        }
        return null;
    }
    
    @Override
    public DisplayableItemNode[] generateNodes() {
        EamDbAttributeInstancesAlgorithm eamDbAttrInst = new EamDbAttributeInstancesAlgorithm();
        CorrelationAttribute corrAttr = eamDbAttrInst.processCorrelationCaseSingleAttribute(crFileId); //TODO which do we want  
        List<DisplayableItemNode> attrInstNodeList = new ArrayList<>(0);
        this.abstractFileReference = Long.getLong(corrAttr.getCorrelationValue());
        
        for (CorrelationAttributeInstance attrInst : corrAttr.getInstances()) {
            tempAttributeInst = attrInst;
            DisplayableItemNode generatedInstNode = generateNode();
            if (generatedInstNode != null) {
                attrInstNodeList.add(generatedInstNode);
            }
        }
        return attrInstNodeList.toArray(new DisplayableItemNode[attrInstNodeList.size()]);
    }
}
