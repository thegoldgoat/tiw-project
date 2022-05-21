type ComponentCallbackEvent = any | undefined
type ComponentCallback = (event: ComponentCallbackEvent) => void

export abstract class Component {
  readonly mountElement: HTMLElement
  readonly subscribers: { [key: string]: ComponentCallback[] } = {}

  constructor(mountElement: HTMLElement) {
    this.mountElement = mountElement
  }

  public mount(): void {
    this.mounted()
    this.showState()
  }

  public update() {
    this.mountElement.innerHTML = ''
    this.showState()
  }

  public addSubscriber(eventName: string, callback: ComponentCallback) {
    if (!this.subscribers[eventName]) this.subscribers[eventName] = []

    this.subscribers[eventName].push(callback)
  }

  public notifySubscribers(eventName: string, event: ComponentCallbackEvent) {
    const subscribersList = this.subscribers[eventName]

    if (!subscribersList) {
      console.warn(
        `Trying to send a notification to subscriber for event ${eventName} but there are no subscribers for this event name`
      )
      return
    }

    subscribersList.forEach((callback) => callback(event))
  }

  protected abstract showState(): void

  protected abstract mounted(): void
}
