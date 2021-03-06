/*
 * MIT License
 *
 * Copyright (c) 2019 BloxBean Project
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.bloxbean.blox4j.pool;

import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ChainConnectionPoolObjectFactory extends BasePooledObjectFactory<ChainConnection> {

    private static final Logger logger = LoggerFactory.getLogger(ChainConnectionPoolObjectFactory.class);

    private ConnectionFactory connectionFactory;

    @Autowired
    public ChainConnectionPoolObjectFactory(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public ChainConnection create() throws Exception {
        return connectionFactory.createConnection();
    }

    public PooledObject<ChainConnection> wrap(ChainConnection chainConnection) {
        return new DefaultPooledObject<ChainConnection>(chainConnection);
    }

    @Override
    public void destroyObject(PooledObject<ChainConnection> p) throws Exception {
        ChainConnection chainConnection = p.getObject();

        if(chainConnection != null) {
            chainConnection.destroy();
        }
    }

    @Override
    public boolean validateObject(PooledObject<ChainConnection> p) {
        if(p.getObject() != null)
            return p.getObject().validate();
        else
            return super.validateObject(p);
    }
}
