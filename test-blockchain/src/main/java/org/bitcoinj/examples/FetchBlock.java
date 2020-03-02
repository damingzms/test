/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoinj.examples;

import java.net.InetAddress;
import java.util.List;
import java.util.concurrent.Future;

import org.bitcoinj.core.Block;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Peer;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.utils.BriefLogFormatter;

import joptsimple.OptionException;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * <p>Downloads the block given a block hash from the remote or localhost node and prints it out.</p>
 * <p>When downloading from localhost, run bitcoind locally: bitcoind -testnet -daemon.
 * After bitcoind is up and running, use command: org.bitcoinj.examples.FetchBlock --localhost &lt;blockHash&gt; </p>
 * <p>Otherwise, use command: org.bitcoinj.examples.FetchBlock &lt;blockHash&gt;, this command will download blocks from a peer generated by DNS seeds.</p>
 */
public class FetchBlock {
    public static void main(String[] args) throws Exception {
        BriefLogFormatter.init();
        // Parse command line arguments
        OptionParser parser = new OptionParser();
        OptionSet opts = null;
        List<byte[]> nonOpts = null;
        try {
            parser.accepts("localhost", "Connect to the localhost node");
            parser.accepts("help", "Displays program options");
            opts = parser.parse(args);
            if (opts.has("help")) {
                System.out.println("usage: org.bitcoinj.examples.FetchBlock [--localhost] <blockHash>");
                parser.printHelpOn(System.out);
                return;
            }
            nonOpts = (List<byte[]>) opts.nonOptionArguments();
            if (nonOpts.size() != 1) {
                throw new IllegalArgumentException("Incorrect number of block hash, please provide only one block hash.");
            }
        } catch (OptionException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("usage: org.bitcoinj.examples.FetchBlock [--localhost] <blockHash>");
            parser.printHelpOn(System.err);
            return;
        }

        // Connect to testnet and find a peer
        System.out.println("Connecting to node");
        final NetworkParameters params = TestNet3Params.get();
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, blockStore);
        PeerGroup peerGroup = new PeerGroup(params, chain);
        if (!opts.has("localhost")) {
            peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        } else {
            PeerAddress addr = new PeerAddress(params, InetAddress.getLocalHost());
            peerGroup.addAddress(addr);
        }
        peerGroup.start();
        peerGroup.waitForPeers(1).get();
        Peer peer = peerGroup.getConnectedPeers().get(0);

        // Retrieve a block through a peer
        Sha256Hash blockHash = Sha256Hash.wrap(nonOpts.get(0));
        Future<Block> future = peer.getBlock(blockHash);
        System.out.println("Waiting for node to send us the requested block: " + blockHash);
        Block block = future.get();
        System.out.println(block);
        peerGroup.stopAsync();
    }
}
