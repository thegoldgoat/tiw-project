import { Component } from './Component'

class EventBus extends Component {
  protected showState(): void {}
  protected mounted(): void {}
}

const eventBus = new EventBus(undefined as any)

export { eventBus }
