.PHONY: build-native
build-native:
	scala-cli \
		--power package \
		--main-class simple_api.MainApp \
		--native-image . \
		-o app \
		-- \
		--no-fallback \
		--trace-class-initialization=org.slf4j.LoggerFactory \
		--trace-class-initialization=io.netty.util.internal.logging.Slf4JLoggerFactory$NopInstanceHolder \
		--trace-class-initialization=io.netty.util.internal.SystemPropertyUtil \
		--trace-object-instantiation=io.netty.util.AsciiString \
		--initialize-at-run-time=io.netty.util.AsciiString \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpObjectAggregator \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpHeaderNames \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpContentDecoder \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpHeaderValues \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpMethod \
		--initialize-at-run-time=io.netty.handler.codec.http.websocketx.WebSocketVersion \
		--initialize-at-run-time=io.netty.handler.codec.http.HttpRequestDecoder \
		--initialize-at-run-time=io.netty.util.internal.PlatformDependent \
		--initialize-at-run-time=org.slf4j.LoggerFactory \
		--initialize-at-run-time=io.netty.util.internal.StringUtil \
		--initialize-at-run-time=io.netty.util.internal.logging.Slf4JLoggerFactory$NopInstanceHolder \
		--initialize-at-run-time=io.netty.util.internal.SystemPropertyUtil \
		--initialize-at-run-time=io.netty.channel.ChannelInitializer \
		--initialize-at-run-time=io.netty.handler.codec.compression.ZstdOptions \
		--initialize-at-run-time=io.netty.handler.ssl.BouncyCastleAlpnSslUtils \
		--initialize-at-run-time=io.netty.channel.DefaultFileRegion \
		--initialize-at-run-time=io.netty.util.AbstractReferenceCounted \
		--initialize-at-run-time=io.netty.channel.DefaultFileRegion	\
		--initialize-at-run-time=io.netty.channel.epoll.Epoll \
		--initialize-at-run-time=io.netty.channel.epoll.Native \
		--initialize-at-run-time=io.netty.channel.epoll.EpollEventLoop \
		--initialize-at-run-time=io.netty.channel.epoll.EpollEventArray \
		--initialize-at-run-time=io.netty.channel.DefaultFileRegion \
		--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventArray \
		--initialize-at-run-time=io.netty.channel.kqueue.KQueueEventLoop \
		--initialize-at-run-time=io.netty.channel.kqueue.Native \
		--initialize-at-run-time=io.netty.channel.unix.Errors \
		--initialize-at-run-time=io.netty.channel.unix.IovArray \
		--initialize-at-run-time=io.netty.channel.unix.Limits \
		--initialize-at-run-time=io.netty.util.internal.logging.Log4JLogger \
		--initialize-at-run-time=io.netty.channel.kqueue.KQueue

.PHONY: run
run:
	./scripts/run.sh
