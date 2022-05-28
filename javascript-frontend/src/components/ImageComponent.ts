import { Image } from '../types/AllImages'
import { Component } from './Component'
import { eventBus } from './EventBus'

const BASE_IMAGE_URL = import.meta.env.BASE_IMAGE_URL | '/tiw-project'

export class ImageComponent extends Component {
  private image: Image
  private addAnchor: boolean

  constructor(mountElement: HTMLElement, image: Image, addAnchor: boolean) {
    super(mountElement)
    this.image = image
    this.addAnchor = addAnchor
  }

  protected mounted(): void {
    const card = document.createElement('div')
    card.classList.add('card')

    const cardTitle = document.createElement('h3')
    cardTitle.classList.add('card-header')
    cardTitle.innerText = this.image.title

    const cardBody = document.createElement('div')
    cardBody.classList.add('card-body')

    const description = document.createElement('h5')
    description.classList.add('card-title')
    description.innerText = this.image.description

    const imageRow = document.createElement('div')
    imageRow.classList.add('row')
    imageRow.classList.add('justify-content-center')

    const imageCol = document.createElement('div')
    imageCol.classList.add('col-auto')

    let imageAnchor
    if (this.addAnchor) {
      imageAnchor = document.createElement('a')
      imageAnchor.href = '#'

      imageAnchor.onclick = (event) => {
        event.preventDefault()
        eventBus.notifySubscribers('gotoImage', this.image.ImagePK)
      }
    } else {
      imageAnchor = undefined
    }

    const imageElement = document.createElement('img')
    imageElement.src = `${BASE_IMAGE_URL}/imageraw?imageId=${this.image.ImagePK}`
    imageElement.alt = this.image.title
    imageElement.style.height = '200px'
    imageElement.classList.add('img-thumbnail')

    if (imageAnchor) {
      imageAnchor.appendChild(imageElement)

      imageCol.appendChild(imageAnchor)
    } else {
      imageCol.appendChild(imageElement)
    }

    imageRow.appendChild(imageCol)

    cardBody.appendChild(description)
    cardBody.appendChild(imageRow)

    const cardFooter = document.createElement('div')
    cardFooter.classList.add('card-footer')
    cardFooter.classList.add('text-muted')
    cardFooter.innerText = this.image.date.toLocaleString()

    card.appendChild(cardTitle)
    card.appendChild(cardBody)
    card.appendChild(cardFooter)

    this.mountElement.appendChild(card)
  }

  protected showState(): void {}
}
