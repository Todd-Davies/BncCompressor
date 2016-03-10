BNC Tools
===========

A suite of tools designed to manipulate the British National Corpus (http://www.natcorp.ox.ac.uk/)
so that it is easier for other applications to use.

Features:
---------
 - Encoder/decoder; maps each token and tag in the BNC to a unique integer. This is useful
   for applications that don't want to handle strings (e.g. GPGPU computation)
 - Stats tools; Currently finds the distribution of sentence lengths of an input corpus, and
   produces a map from each token to the most common tag for the token.
 