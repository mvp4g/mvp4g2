package gwt.mvp4g.client.mvp;

/**
 * Created by hoss on 19.09.16.
 */
public interface IsView<P> {

  P getPresenter();

  void setPresenter(P presenter);

}
