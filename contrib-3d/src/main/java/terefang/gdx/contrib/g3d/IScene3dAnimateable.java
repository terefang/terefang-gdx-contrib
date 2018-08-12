/*
 * Copyright (c) 2018. terefang@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package terefang.gdx.contrib.g3d;

import java.util.Collection;

public interface IScene3dAnimateable
{
	public void onAnimate(long timeMs);
	
	public Collection<IScene3dNodeAnimator> getSceneNodeAnimators();
	
	public void setSceneNodeAnimators(Collection<IScene3dNodeAnimator> scene3dNodeAnimators);
	
	public void addSceneNodeAnimator(IScene3dNodeAnimator scene3dNodeAnimator);
	
	public void animateStart();
	public void animatePause();
	public void animateStop();
	
}
