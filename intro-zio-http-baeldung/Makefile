.PHONY: build-native
build-native:
	scala-cli \
		--power package \
		--main-class simple_api.MainApp \
		--native-image . \
		-o app \
		-- \
		--no-fallback \
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
	env $(cat .env | xargs) scala-cli . --main-class simple_api.MainApp