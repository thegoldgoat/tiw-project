import { Component } from './Component'

export type ToastMessage = {
  isError: boolean
  message: string
}

const HIDE_TIMEOUT = 5000

export class Toast extends Component {
  private isError = true
  private message = ''
  private hideTimeout!: number

  private toastDiv!: HTMLDivElement
  private toastBody!: HTMLDivElement

  updateToast(msg: ToastMessage) {
    this.isError = msg.isError
    this.message = msg.message

    this.update()
  }

  protected mounted(): void {
    this.toastDiv = document.createElement('div')
    this.toastDiv.classList.add('toast')
    this.toastDiv.classList.add('align-items-center')
    this.toastDiv.classList.add('text-white')
    this.toastDiv.classList.add('border-0')

    this.toastDiv.onclick = () => {
      this.hideToast()
    }

    const flexDiv = document.createElement('div')
    flexDiv.classList.add('d-flex')

    this.toastBody = document.createElement('div')
    this.toastBody.classList.add('toast-body')

    flexDiv.appendChild(this.toastBody)

    this.toastDiv.appendChild(flexDiv)

    this.mountElement.appendChild(this.toastDiv)
  }

  private hideToast() {
    this.toastDiv.classList.remove('show')
  }

  protected showState(): void {
    if (!this.message) {
      this.hideToast()
      return
    }

    this.toastBody.innerText = this.message

    if (this.isError) {
      this.toastDiv.classList.remove('bg-success')
      this.toastDiv.classList.add('bg-danger')
    } else {
      this.toastDiv.classList.add('bg-success')
      this.toastDiv.classList.remove('bg-danger')
    }

    this.toastDiv.classList.add('show')

    clearTimeout(this.hideTimeout)
    this.hideTimeout = setTimeout(() => this.hideToast(), HIDE_TIMEOUT)
  }
}

// <div class="toast align-items-center text-white bg-primary border-0" role="alert" aria-live="assertive" aria-atomic="true">
//   <div class="d-flex">
//     <div class="toast-body">
//       Hello, world! This is a toast message.
//     </div>
//     <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
//   </div>
// </div>
