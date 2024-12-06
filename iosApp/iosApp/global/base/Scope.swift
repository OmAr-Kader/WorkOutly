import Foundation


@inline(__always) @discardableResult func TaskBackSwitcher(
    block: @BackgroundActor @escaping () async -> Void
) -> Task<Void, Error>? {
    return Task { @BackgroundActor in
        return await block()
    }
}


@inline(__always) @discardableResult func TaskMainSwitcher(
    block: @MainActor @escaping () async -> Void
) -> Task<Void, Error>? {
    return Task { @MainActor in
        return await block()
    }
}

@frozen public struct Scope : Sendable {

    var backTask: Task<Void, Error>?
    var medTask: Task<Void, Error>?
    var realmTask: Task<Void, Error>?
    
    var mainTask: Task<Void, Error>?

    @inline(__always) @discardableResult mutating func launch(
        block: @Sendable @escaping () async -> Void
    ) -> Task<Void, Error>? {
        backTask = Task(priority: .background) { [backTask] in
            let _ = await backTask?.result
            return await block()
        }
        return backTask
    }
    
    @inline(__always) @discardableResult mutating func launchMed(
        block: @Sendable @escaping () async -> Void
    ) -> Task<Void, Error>? {
        medTask = Task(priority: .medium) { [medTask] in
            let _ = await medTask?.result
            return await block()
        }
        return realmTask
    }

    @inline(__always) @discardableResult mutating func launchMain(
        block: @MainActor @escaping @Sendable () async -> Void
    ) -> Task<Void, Error>? {
        mainTask = Task { @MainActor [mainTask] in
            let _ = await mainTask?.result
            return await block()
        }
        return mainTask
    }
    
    @inline(__always) @discardableResult func launchBack(
        block: @BackgroundActor @escaping () async -> Void
    ) -> Task<Void, Error>? {
        return Task { @BackgroundActor in
            return await block()
        }
    }
    
    mutating func deInit() {
        backTask?.cancel()
        medTask?.cancel()
        realmTask?.cancel()
        mainTask?.cancel()
        self.backTask = nil
        self.medTask = nil
        self.realmTask = nil
        self.mainTask = nil
    }
}


class Scoper {

    var backTask: Task<Void, Error>? = nil
    var medTask: Task<Void, Error>? = nil
    var realmTask: Task<Void, Error>? = nil
    
    var mainTask: Task<Void, Error>? = nil

    @inline(__always) @discardableResult func launch(
        block: @Sendable @escaping () async -> Void
    ) -> Task<Void, Error>? {
        backTask = Task(priority: .background) { [backTask] in
            let _ = await backTask?.result
            return await block()
        }
        return backTask
    }
    
    @inline(__always) @discardableResult func launchMed(
        block: @Sendable @escaping () async -> Void
    ) -> Task<Void, Error>? {
        medTask = Task(priority: .medium) { [medTask] in
            let _ = await medTask?.result
            return await block()
        }
        return realmTask
    }

    @inline(__always) @discardableResult func launchMain(
        block: @MainActor @escaping @Sendable () async -> Void
    ) -> Task<Void, Error>? {
        mainTask = Task { @MainActor [mainTask] in
            let _ = await mainTask?.result
            return await block()
        }
        return mainTask
    }
    
    @inline(__always) @discardableResult func launchBack(
        block: @BackgroundActor @escaping () async -> Void
    ) -> Task<Void, Error>? {
        return Task { @BackgroundActor in
            return await block()
        }
    }
    
    deinit {
        backTask?.cancel()
        medTask?.cancel()
        realmTask?.cancel()
        mainTask?.cancel()
        self.backTask = nil
        self.medTask = nil
        self.realmTask = nil
        self.mainTask = nil
    }
}


protocol ScopeFunc {}
extension NSObject: ScopeFunc {}
extension Array : ScopeFunc {}
extension Int : ScopeFunc {}
extension Float : ScopeFunc {}


extension Optional where Wrapped: ScopeFunc {

    @inline(__always) func `let`<R>(_ block: (Wrapped) -> R) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }
    
    @BackgroundActor
    @inline(__always) func letBack<R>(_ block: @BackgroundActor (Wrapped) -> R) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }
    
    
    @BackgroundActor
    @inline(__always) func letBackN<R>(_ block: @BackgroundActor (Wrapped?) -> R?) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }

    @BackgroundActor
    @inline(__always) func letSusBack<R: Sendable>(_ block: @BackgroundActor (Wrapped) async -> R) async -> R? {
        guard let self = self else { return nil }
        return await block(self)
    }
    
    @inline(__always) func apply(_ block: (Self) -> ()) -> Self? {
        guard let self = self else { return nil }
        block(self)
        return self
    }
    
}


extension Optional where Wrapped == ScopeFunc? {

    @inline(__always) func `let`<R: Sendable>(_ block: (Wrapped) -> R) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }
    
    @BackgroundActor
    @inline(__always) func letBack<R: Sendable>(_ block: @BackgroundActor (Wrapped) -> R) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }
    
    @BackgroundActor
    @inline(__always) func letBackN<R: Sendable>(_ block: @BackgroundActor (Wrapped?) -> R?) -> R? {
        guard let self = self else { return nil }
        return block(self)
    }
    
    @BackgroundActor
    @inline(__always) func letSusBack<R: Sendable>(_ block: @BackgroundActor (Wrapped) async -> R) async -> R? {
        guard let self = self else { return nil }
        return await block(self)
    }
    
    @inline(__always) func apply(_ block: (Self) -> ()) -> Self {
        guard let self = self else { return nil }
        block(self)
        return self
    }
}



extension Optional {
    func `let`(do: (Wrapped)->()) {
        guard let v = self else { return }
        `do`(v)
    }
}

extension ScopeFunc {
    
    @inline(__always) func apply(_ block: (Self) -> ()) -> Self {
        block(self)
        return self
    }
    
    @inline(__always) func supply(_ block: (Self) -> ()) {
        block(self)
    }
    
    @BackgroundActor
    @inline(__always) func applyBack(_ block: @BackgroundActor (Self) -> ()) -> Self {
        block(self)
        return self
    }
    
    @BackgroundActor
    @inline(__always) func supplyBack(_ block: @BackgroundActor (Self) -> ()) {
        block(self)
    }
    
    @inline(__always) func `let`<R>(_ block: (Self) -> R) -> R {
        return block(self)
    }
    
}

extension Task where Success == Never, Failure == Never {
  static func sleep(seconds: TimeInterval) async {
    try? await Task.sleep(nanoseconds: UInt64(seconds * 1_000_000_000))
  }
}


@globalActor actor BackgroundActor: GlobalActor {
    static var shared = BackgroundActor()
}
