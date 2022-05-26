export type Image = {
  ImagePK: number
  title: string
  description: string
  path: string
  date: Date
}

export type AllImages = {
  images: Image[]
  pageCount: number
  currentPage: number
}
