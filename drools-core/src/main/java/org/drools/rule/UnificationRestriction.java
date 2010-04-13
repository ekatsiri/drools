package org.drools.rule;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

import org.drools.common.InternalFactHandle;
import org.drools.common.InternalWorkingMemory;
import org.drools.reteoo.LeftTuple;
import org.drools.rule.VariableRestriction.VariableContextEntry;
import org.drools.spi.AcceptsReadAccessor;
import org.drools.spi.Evaluator;
import org.drools.spi.InternalReadAccessor;
import org.drools.spi.Restriction;
import org.drools.time.Interval;

public class UnificationRestriction
    implements
    AcceptsReadAccessor,
    Restriction {
    
    VariableRestriction vr;
    Declaration declaration;
    
    public UnificationRestriction() {
        
    }
    
    public UnificationRestriction(VariableRestriction vr) {
        this.vr = vr;
        this.declaration = vr.getRequiredDeclarations()[0];
    }

    public ContextEntry createContextEntry() {
        return new UnificationContextEntry(this.vr.createContextEntry(), declaration);
    }

    public Declaration[] getRequiredDeclarations() {
        return this.vr.getRequiredDeclarations();
    }

    public boolean isAllowed(InternalReadAccessor extractor,
                             InternalFactHandle handle,
                             InternalWorkingMemory workingMemory,
                             ContextEntry context) {
        throw new UnsupportedOperationException( "Cannot use a Unification Restriction in the AlphaNetwork" );
    }

    public boolean isAllowedCachedLeft(ContextEntry context,
                                       InternalFactHandle handle) {
        if ( ((UnificationContextEntry)context).getVariable() == null ) {
            return this.vr.isAllowedCachedLeft( ((UnificationContextEntry)context).getContextEntry(), handle );
        } else {
            VariableContextEntry vContext =  (VariableContextEntry) ((UnificationContextEntry)context).getContextEntry();
            ((UnificationContextEntry)context).getVariable() .setValue( vContext.getFieldExtractor().getValue(handle.getObject() ) );
            return true;
        }
    }

    public boolean isAllowedCachedRight(LeftTuple tuple,
                                        ContextEntry context) {
        throw new UnsupportedOperationException( "Cannot right activate a Unification Restriction (for now )" );
    }
    
    public Evaluator getEvaluator() {
        return this.vr.getEvaluator();
    }
    
    public Interval getInterval() {
        return this.vr.getInterval();
    }

    public boolean isTemporal() {
        return false;
    }

    public void replaceDeclaration(Declaration oldDecl,
                                   Declaration newDecl) {
        this.vr.replaceDeclaration( oldDecl, newDecl );
        this.declaration = vr.getRequiredDeclarations()[0];
    }
    
    

    public void readExternal(ObjectInput in) throws IOException,
                                            ClassNotFoundException {
        this.vr = ( VariableRestriction ) in.readObject();
        this.declaration = vr.getRequiredDeclarations()[0];
    }

    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject( this.vr );
    }

    public Object clone() {
        return new UnificationRestriction( (VariableRestriction) this.vr.clone() );
    }
    
    public void setReadAccessor(InternalReadAccessor readAccessor) {
        this.vr.setReadAccessor( readAccessor );
        this.declaration = vr.getRequiredDeclarations()[0];
    }    
    
    public static class UnificationContextEntry implements ContextEntry {
        private ContextEntry contextEntry;
        private Declaration declaration;
        private Variable variable;
        
        public UnificationContextEntry(ContextEntry contextEntry,
                                       Declaration declaration) {
            this.contextEntry = contextEntry;
            this.declaration = declaration;
        }


        public ContextEntry getContextEntry() {
            return this.contextEntry;
        }
        

        public ContextEntry getNext() {
            return this.contextEntry.getNext();
        }

        public void resetFactHandle() {
            this.contextEntry.resetFactHandle();
        }

        public void resetTuple() {
            this.contextEntry.resetTuple();
        }

        public void setNext(ContextEntry entry) {
            this.contextEntry.setNext( entry );
        }

        public void updateFromFactHandle(InternalWorkingMemory workingMemory,
                                         InternalFactHandle handle) {
            throw new UnsupportedOperationException( "Cannot right activate a Unification Restriction (for now )" );
            //this.contextEntry.updateFromFactHandle( workingMemory, handle );
        }

        public void updateFromTuple(InternalWorkingMemory workingMemory,
                                    LeftTuple tuple) {
            Object object = this.declaration.getValue( workingMemory, tuple.get( 0 ).getObject() );
            if ( !(object instanceof Variable) ) {
                this.variable = null;
                this.contextEntry.updateFromTuple( workingMemory, tuple );    
            } else {                
                this.variable = (Variable) object;
            }
        }

        public void readExternal(ObjectInput in) throws IOException,
                                                ClassNotFoundException {
            this.contextEntry = (ContextEntry) in.readObject();
            this.declaration = ( Declaration ) in.readObject();
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject( this.contextEntry );
            out.writeObject( this.declaration );
        }
        
        public Variable getVariable() {
            return this.variable;
        }
        
    }



}