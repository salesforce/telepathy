# telepathy

> Telepathy is the purported vicarious transmission of information from one
> person to another without using any known human sensory channels or physical
> interaction.

[![CircleCI](https://circleci.com/gh/salesforce/telepathy.svg?style=svg)](https://circleci.com/gh/salesforce/telepathy)

A Scala library that wraps okhttp and circe to make it easy to send a http call
and get JSON response.

## Configuration

Add the dependency to your `build.sbt` file

```
libraryDependencies += "com.salesforce.mce" %% "telepathy" % "_VERSION_"
```

## Usage

This is a relatively small library, look at `HttpRequest` object to see how to
use it.
