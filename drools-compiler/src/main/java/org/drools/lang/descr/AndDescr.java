/*
 * Copyright 2005 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.drools.lang.descr;

import java.util.ArrayList;
import java.util.List;

public class AndDescr extends BaseDescr
    implements
    ConditionalElementDescr {
    private static final long serialVersionUID = 510l;
    private List<BaseDescr>    descrs           = new ArrayList<BaseDescr>();

    public AndDescr() { }

    public void addDescr(final BaseDescr baseDescr) {
        this.descrs.add( baseDescr );
    }

    public void insertDescr(int index,
                            final BaseDescr baseDescr) {
        this.descrs.add( index,
                         baseDescr );
    }

    public void insertBeforeLast(final Class<?> clazz,
                             final BaseDescr baseDescr) {
        if ( this.descrs.isEmpty() ) {
            addDescr( baseDescr );
            return;
        }

        for ( int i = this.descrs.size() - 1; i >= 0; i-- ) {
            if ( clazz.isInstance( this.descrs.get( i ) ) ) {
                insertDescr( i,
                             baseDescr );
                return;
            }
        }
        
        addDescr( baseDescr );
    }

    public List<BaseDescr> getDescrs() {
        return this.descrs;
    }

    public void addOrMerge(final BaseDescr baseDescr) {
        if ( baseDescr instanceof AndDescr ) {
            for( BaseDescr descr : ((AndDescr) baseDescr).getDescrs() ) {
                addDescr( descr );
            }
        } else {
            addDescr( baseDescr );
        }
    }

    public boolean removeDescr(BaseDescr baseDescr) {
        return baseDescr == null ? false : descrs.remove(baseDescr);
    }



    public String toString() {
        return "[AND "+descrs+" ]";
    }
}
