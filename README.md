# ProjectPos_2026 — PoS System

A small supermarket point-of-sale system implementing the two core use cases
**Process Sale** and **Handle Returns**, built with object-oriented techniques
and the design artefacts described in the technical report.

## Build and run

    javac -encoding UTF-8 -d out src/pos/*.java src/pos/ui/*.java
    java -cp out pos.Main            # Swing application
    java -cp out pos.ConsoleDemo     # headless walk-through of both use cases

## Layout

    src/pos        domain layer (Sale, Return, Payment, Register, ...)
    src/pos/ui     presentation layer (Swing frames and panels)

## Branches

    feature/process-sale       core sale loop
    feature/payments-receipts  cash and card payment, receipts
    feature/returns            Handle Returns screen and refund
